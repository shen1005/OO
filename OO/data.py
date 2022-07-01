import os

fp = open("stdin.txt", "w")
fp.write("")
fp = open("stdin.txt", "a")
i = 1
while True:
    n = input()
    if n == "END":
        break
    else:
        i += 1
        li = n.split()
        fp.write("[0.0]" + str(i) + "-FROM-" + li[0] + "-" + li[1] + "-TO-" + li[0] + "-" + li[2] + "\n")
os.popen("datainput_student_win64.exe | java -jar code.jar>ANS.txt")
