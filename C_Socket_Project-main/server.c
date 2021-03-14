#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <signal.h>
#include <wait.h>
#include <errno.h>
#include <fcntl.h>

#define MAX 4096
void error(const char *msg)
{
    perror(msg);
    exit(1);
}

void signalhandler(int signum)
{
    printf("Exited with ctrl-c\n");
    printf("Caught signal %d\n",signum);
    exit(signum);
}

void login(int newsockfd) {
    char password[20];
    do {
        printf("Reading password from client...\n");
        bzero(password,20);
        int n = read(newsockfd,password,20);
        if ( n <= 0) {
            printf("Error reading from socket\n");
            printf("Possible explanation: Client exit: signal 15\n");
            exit(0);
        }
        // printf("password given: %s\n",password);

        if ( strcmp(password,"0000") != 0 ) {
            printf("Incorrect password!\n");
            n = write(newsockfd, "Incorrect password, please try again: ", 40);
            if ( n <= 0) {
                error("ERROR writing to socket\n");
            }
        }
        else {
            printf("Authentication completed! Logging in...\n");
            n = write(newsockfd," ",2);
            if ( n <= 0) {
                error("ERROR writing to socket\n");
            }
        }
    } while (strcmp(password,"0000") != 0);
}

void pipe_function(char*args1[], char* args2[] ) {
    int fd[2];
    pipe(fd);
    pid_t pid = fork();
    char path[MAX];
    memset(path, 0, MAX - 1);

    if (pid == 0) {
        dup2(fd[1], 1);
        close(fd[0]);
        close(fd[1]);
        execvp(args1[0], args1);
        // fprintf(stdout,"Error executing command: %s\n",strerror(errno));

    } else if (pid > 0) {
        dup2(fd[0], 0);
        close(fd[1]);
        int status;
        waitpid(pid, &status, 0);
        execvp(args2[0], args2);
        fprintf(stdout,"Error executing command: %s\n",strerror(errno));
    }
}


void setup(char buffer[],char *args[50],char *args_aux[50],int haspipe,int redirection) {

    char delim[] =  {' ','\t','\n','\0'};
    char *token = strtok(buffer, delim);
    int i=0;

    if (haspipe) {

        int j=0;
        while( (token!=NULL ) ) {
            if( (strcmp(token,"|") == 0) ) {
                token = strtok(NULL, delim);
                break;
            }
            args[i++] = token;
            token = strtok(NULL, delim);
        }

        while ( (token!=NULL ) ) {
            args_aux[j++] = token;
            token = strtok(NULL, delim);
        }
        args_aux[j] = NULL;

    } else if (redirection){

        int j=0;
        while( (token!=NULL ) ) {
            if( (strcmp(token,">") == 0) ) {
                token = strtok(NULL, delim);
                break;
            }
            args[i++] = token;
            token = strtok(NULL, delim);
        }

        while ( (token!=NULL ) ) {
            args_aux[j++] = token;
            token = strtok(NULL, delim);
        }
        args_aux[j] = NULL;



    } else {

        while (token != NULL) {
            args[i++] = token;
            token = strtok(NULL, delim);
        }

    }
    args[i] = NULL;
}

void HandleClient(int newsockfd,char *str) {

    //int j is flag for disconnecting client
    int j=0;
    char buffer[MAX];
    int bytes;

    //communication: read/write with client
    while (1) {

        //Reading command from client
        bzero(buffer, MAX);
        bytes = read(newsockfd, buffer, MAX-1);

        //Checking for pipe
        char *has_pipe = (strchr(buffer,'|'));

        //checking for simple redirection
        char *has_redirection = (strchr(buffer,'>'));


        int haspipe;
        if (has_pipe != NULL) {
            haspipe = 1;
        }else {
            haspipe = 0;
        }

        int redirection;
        if (has_redirection != NULL) {
            redirection = 1;
        } else {
            redirection = 0;
        }


        //checking for exiting client
        if ((strcmp("exit", buffer) == 0))
            j = 1;

        if (bytes <= 0) {
            printf("Error reading from socket\n");
            printf("Possible explanation: Client exit: signal 15\n");
            exit(0);
        }
        printf("Here is the message: %s\n", buffer);


        //client exiting after the server has received "exit"
        if (j) {
            printf("Client %s disconnecting...\n", str);
            exit(0);
        } else {

            char *args[50];
            char *args_aux[50];
            //setting up tokens
            setup(buffer, args, args_aux,haspipe,redirection);
            int fd[2],length,p;
            pipe(fd);


            //checking for cd

            if (strcmp(args[0], "cd") == 0) {
                write(newsockfd,"Command executed succesfully\n",35);
                if (args[1] != NULL) {
                    chdir(args[1]);
                } else {
                    chdir("..");
                }
            } else if (strcmp(args[0], "history") == 0 ) {
                write(newsockfd, "Command executed succesfully\n", 35);
            } else {

                pid_t pid = fork();
                char path[MAX];
                memset(path, 0, MAX-1);

                if (pid == 0) {
                    dup2(fd[1], 1);
                    dup2(fd[1],2);
                    close(fd[0]);
                    close(fd[1]);

                    if(haspipe) {
                        pipe_function(args,args_aux);
                        exit(-4);
                    } else if(redirection){
                        //if one arg was given as the filename -> proceed
                        if( args_aux[1] == NULL) {

                            p = open(args_aux[0], O_WRONLY | O_CREAT,0777);
                            dup2(p,1);
                            close(p);
                            execvp(args[0],args);
                            exit(-1);

                        } else {
                            fprintf(stdout,"ERROR: Wrong redirection file name, more than 1 arguments\n");
                            exit(-2);
                        }
                    }
                    else {
                        execvp(args[0], args);
                        fprintf(stdout,"Error executing command: %s\n",strerror(errno));
                        exit(-3);
                    }



                } else if( pid > 0) {
                    dup2(fd[0],0);
                    close(fd[1]);
                    bzero(buffer,MAX);
                    int status;

                    waitpid(pid, &status, 0);

                    if(redirection)
                    {
                        if ( status == 65280) {
                            write(newsockfd,"ERROR: first command of redirection not found\n",50);
                        } else if ( status == 0) {
                            write(newsockfd,"Command executed successfully\n",35);
                        }

                    }


                    while ( (length = read(fd[0], path, sizeof(path)-1)) ) {
                        path[length] = '\0';
                        if (write(newsockfd, path, strlen(path)) != strlen(path)) {
                            error("ERROR write");
                        }
                         bzero(buffer,MAX);
                    }
                    close(fd[0]);


                } else {
                    error("ERROR fork");
                }
            }
        }

    }

}

void HarvestZombies(int sig)
{
    while(wait(NULL) > 0);
}


int main(int argc, char *argv[]) {
    //register signal handling
    signal(SIGTERM, signalhandler);
    signal(SIGCHLD,HarvestZombies);
    pid_t childpid;
    int child_count=0;
    int sockfd, newsockfd, portno;
    socklen_t clilen;
    struct sockaddr_in serv_addr, cli_addr;

    char str[INET_ADDRSTRLEN];

    if (argc < 2) {
        fprintf(stderr, "No port provided\n");
        exit(1);
    }

    //creating main socket
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0)
        error("ERROR opening socket");
    printf("[+]Server socket created\n");

    /* bzero((char *) &serv_addr, sizeof(serv_addr)); */
    memset((char *) &serv_addr, 0, sizeof(serv_addr));
    portno = atoi(argv[1]);

    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = INADDR_ANY;
    serv_addr.sin_port = htons(portno);

    if (bind(sockfd, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0)
        error("ERROR on binding");
    printf("[+]Bind to port %s\n",argv[1]);

    if (listen(sockfd, 5) != 0) {
        printf("ERROR: Listen failed");
        exit(0);
    } else
        printf("Server listening...\n");


    //creating connection queue
    for(;;) {

        printf(" -- Waiting for new possible connections -- \n");

        //accepting connection
        clilen = sizeof(cli_addr);
        newsockfd = accept(sockfd, (struct sockaddr *) &cli_addr, &clilen);

        //checking for errors of accept()
        if (newsockfd < 0)
            error("ERROR on accept");

        if (inet_ntop(AF_INET, &cli_addr.sin_addr, str, INET_ADDRSTRLEN) == NULL) {
            fprintf(stderr, "Could not convert byte to address\n");
            exit(1);
        }


        childpid = fork();
        if (childpid < 0)
            error("ERROR fork");


        if (childpid == 0) {
            //client log in
            login(newsockfd);

            //connection done
            printf("New client connected. \n");
            fprintf(stdout, "The client address is :%s\n", str);
            HandleClient(newsockfd, str);
        }


        close(newsockfd);
        child_count++;


    }
}
