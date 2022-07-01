# -*- coding: utf-8 -*-
from Diagram import Diagram
from subprocess import Popen, PIPE, STDOUT

def execute_java(jar_path, input_s):
    command = "java -jar " + jar_path
    proc = Popen(command, stdin=PIPE, stdout=PIPE, stderr=STDOUT)
    stdout, stderr = proc.communicate(input_s.encode())
    return stdout.decode()


def gen():
    d = Diagram()
    ret = d.getData()
    input_s = ""
    for s in ret:
        input_s = input_s + s + "\n"
    return input_s


def check_once():
    input_s = gen()
    f = open("input.txt", "w")
    f.write(input_s)
    f.close()
    # pai = r"C:\Users\Lenovo\Desktop\OO\3-1\out\artifacts\3_1_jar\3-1.jar"
    # pai_res = execute_java(s, pai)
    # print("finish pai")
    my = r"C:\Users\szc\Desktop\szc.jar"
    my_res = execute_java(my, input_s)
    # print(my_res)
    print("finish my")
    pai = r"C:\Users\szc\Desktop\wjy.jar"
    pai_res = execute_java(pai, input_s)
    print("finish pai")
    if my_res != pai_res:
        print("wrong answer")
        # print("============================")
        f = open("wjy.txt", "w")
        f.write(pai_res)
        f.close()
        # print(pai_res)
        f = open("szc.txt", "w")
        f.write(my_res)
        f.close()
        # print("============================")
        # print(my_res)
        return False
    else:
        return True


if __name__ == "__main__":
    ct = 0
    while True:
        ct = ct + 1
        print("start build case : " + str(ct))
        if check_once() is False:
            break
