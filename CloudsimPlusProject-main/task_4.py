import random
from sko.GA import GA


def task_generation():
    ltask_times = []
    for i in range(0, 10):
        ltask_times.append(random.randint(20, 700))
    return ltask_times


def evaluate(x):
    sum_CPU_1 = 0
    sum_CPU_2 = 0
    for i in range(10):
        if x[i] == 0:
            sum_CPU_1 += tasks[i]

    for i in range(10):
        if x[i] == 1:
            sum_CPU_2 += tasks[i]

    return abs(sum_CPU_1 - sum_CPU_2)


def task_schedule(best_x):
    sum_CPU_1 = 0
    sum_CPU_2 = 0
    names = []
    for i in range(10):
        if best_x[i] == 0:
            names.append(i)
            sum_CPU_1 += tasks[i]
    Z = [x for _, x in sorted(zip(tasks, names))]
    print("tasks that goes to CPU-1:", Z)

    names = []
    for i in range(10):
        if best_x[i] == 1:
            names.append(i)
            sum_CPU_2 += tasks[i]
    Z = [x for _, x in sorted(zip(tasks, names))]
    print("tasks that goes to CPU-2:", Z)

    return max(sum_CPU_1, sum_CPU_2)


if __name__ == '__main__':
    for i in range(10):
        tasks = task_generation()

        ga = GA(func=evaluate, n_dim=10, size_pop=50,
                lb=[0 for j in range(10)], ub=[1 for j in range(10)],
                max_iter=100, precision=1)
        best_x, best_y = ga.run()

        print("{}-task minibatch ".format(i + 1))
        
        print("Random Task Offloading.")
        prob_executed_CPU_1 = [random.randint(0, 1) for j in range(10)]
        total_execution_time = task_schedule(prob_executed_CPU_1)
        print("Random Makespan: ", total_execution_time, "msec")
        print("Random Throughput: ", "%.2f" % (10_000 / total_execution_time), "(tasks/sec)\n")

        print("Intelligent Task Offloading.")
        total_execution_time = task_schedule(best_x)
        print("Intelligent Makespan: ", total_execution_time, "msec")
        print("Intelligent Throughput: ", "%.2f" % (10_000 / total_execution_time), "(tasks/sec)\n")
