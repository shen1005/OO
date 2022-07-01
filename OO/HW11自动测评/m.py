f1 = open("1_out.txt", "r")
f2 = open("4_out.txt", "r")
out1 = f1.readlines()
out2 = f2.readlines()
ans = []
for i in range(0, len(out1)-1):
    j = i
    if out1[i] != out2[i]:
        ans.append(i)
for i in ans:
    print(i)