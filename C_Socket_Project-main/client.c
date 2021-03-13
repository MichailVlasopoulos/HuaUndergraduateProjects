#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <signal.h>


#define MAX 4096

char filename[32] = "history";
int hasgivenpassword = 0;

void error(const char *msg)
{
    perror(msg);
    exit(0);
}

void signalhandler(int signum)
{
    printf("\nExited with ctrl-c\n");
    printf("Caught signal %d\n",signum);
    //file history is created after passsword is given, checks for unreasonable delete of file
    if ( hasgivenpassword == 1) {
        int m = remove(filename);
        if(m < 0)
        {
            error("ERROR remove");
        }
    }

    exit(signum);
}

int Write(char* str,int lines) {

    FILE* file;
    file = fopen(filename,"a");

    if (file != NULL) {
        fseek(file,0,SEEK_END);
        fprintf(file,"%s\n",str);
        lines++;
        fclose(file);
    }else {
        printf("File could not be opened to write a command\n");
    }

    return lines;
}

void History() {

    FILE* file;
    char *line = NULL;
    size_t len=0;
    ssize_t read;
    file = fopen(filename,"r");

    if (file != NULL) {

        int i = 1;
        while((read = getline(&line,&len,file)) != -1) {
                printf("%d. %s",i,line);
                i++;
        }

    }else {
        printf("File could not be opened to read previous commands\n");
    }
    fclose(file);
}


int main(int argc, char *argv[])
{
    //register signal handling
    signal(SIGTERM, signalhandler);

    int sockfd, portno, n;
    struct sockaddr_in serv_addr;
    struct hostent *server;
    pid_t lol = getpid();
    sprintf(filename,"%s%d",filename,lol);

    char buffer[MAX];
    if (argc < 3)
    {
        fprintf(stderr, "usage %s hostname port\n", argv[0]);
        exit(0);
    }
    portno = atoi(argv[2]);

    //socket(IPv4, type: stream socket, protocol:default
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0)
        error("ERROR opening socket");
    server = gethostbyname(argv[1]);
    if (server == NULL)
    {
        fprintf(stderr, "ERROR, no such host\n");
        exit(0);
    }
    bzero((char *) &serv_addr, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    bcopy((char *)server->h_addr,
          (char *)&serv_addr.sin_addr.s_addr,
          server->h_length);
    serv_addr.sin_port = htons(portno);

    //connecting
    if (connect(sockfd, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0)
        error("ERROR connecting");


    do {

        bzero(buffer, MAX);
        do {
            printf("%s _> Please enter your password: ",argv[1]);
            fgets(buffer, MAX - 1, stdin);
            //handling the new line
            buffer[strcspn(buffer,"\n")] = 0;
        } while ( !buffer[0] );

        n = write(sockfd, buffer, strlen(buffer));
        // printf("password writen: %s\n",buffer);
        if (n < 0)
            error("ERROR writing to socket");
        bzero(buffer, MAX);
        n = read(sockfd, buffer, MAX-1);
        printf("%s\n",buffer);
        if (n < 0)
            error("ERROR reading from socket\n");

    } while (strcmp(buffer," ") != 0);


    printf("Connected to server: %s\n",argv[1]);
    //connected
    int f=0;
    int lines=0;
    while(1) {

        do {
            printf("%s _> Please enter the message: ", argv[1]);
            bzero(buffer, MAX);
            fgets(buffer, MAX - 1, stdin);
            //handling the new line
            buffer[strcspn(buffer,"\n")] = 0;
        } while ( !buffer[0] );


        //Writing to file for "history" command purpose
        hasgivenpassword = 1;
        Write(buffer,lines);


        //sending message to server
        n = write(sockfd, buffer, strlen(buffer));
        if (n < 0) {
            error("ERROR writing to socket");
        }


        //checking for "exit"
        if (strcmp("exit", buffer) == 0)  {
            f=1;
        }

        //checking for "history"
        if (strcmp("history", buffer) == 0) {
            History();
            // continue;
        }


        //reading from server
        bzero(buffer, MAX);
        n = read(sockfd, buffer, MAX-1);
        if (n < 0)
            error("ERROR reading from socket\n");
        else {
            printf("Received message from server\n");
            printf("%s", buffer);
        }

        if(f) {
            printf("Exiting the server...\n");
            break;
        }
    }
    int m = remove(filename);
    if(m < 0)
    {
        error("ERROR remove");
    }
    close(sockfd);
    return 0;
}
