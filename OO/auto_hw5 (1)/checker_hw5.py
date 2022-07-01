import subprocess
from sys import stdout

class Elevator:
    def __init__(self, string):
        string = RemoveTime(string)
        msg = string.split("-")
        self.type = msg[1]
        self.elvId = msg[2]
        self.ca = int(msg[4])
        self.speed = float(msg[5])
        if self.type == "floor":
            self.mask = int(msg[-1])
        else:
            self.mask = 31
        self.position = msg[3]
        self.message = []

    def addMessage(self, string):
        self.message.append(string)

class Person:
    def __init__(self, string):
        msg = string.split("-")
        self.id = msg[0]
        self.nowBlock = msg[2]
        self.nowFloor = msg[3]
        self.finalBlock = msg[5]
        self.finalFloor = msg[6]
        self.message = []
    def setNow(self, string):
        msg =string.split("-")
        self.nowBlock = msg[2]
        self.nowFloor = msg[3]

def RemoveTime(string):
    return string.split("]")[1]
    
def RemoveEnter(string):
    return string.split("\n")[0]

def ParseKind(string): ##根据string分类 0:Arrive 1:OPEN 2:CLOSE 3:IN 4:OUT
    string_noTime = RemoveTime(string) ## 去除时间戳
    kind = string_noTime.split("-")[0]
    if kind=="ARRIVE":
        return 0
    elif kind == "OPEN":
        return 1
    elif kind == "CLOSE":
        return 2
    elif kind == "IN":
        return 3
    else:
        return 4

def ParseTime(string):
    time  = string.split("]")[0]
    time = time.split("[")[1]
    time = time.split()[0]
    time = float(time)
    return time


def ParseArriveOpenClose(string):
    time = ParseTime(string)
    string = RemoveTime(string)
    msgList = string.split("-")
    building = msgList[1]
    curPos = int(msgList[2])
    id = int(msgList[3])
    return [time,building,curPos,id]


def ParseInOut(string):
    time = ParseTime(string)
    string = RemoveTime(string)
    msgList = string.split("-")
    pId = int(msgList[1])
    building = msgList[2]
    curPos = int(msgList[3])
    eId = int(msgList[4])
    return [time,pId,building,curPos,eId]


def ParseBuilding(string):
    kind = ParseKind(string)
    if kind==0 or kind==1 or kind == 2:
        msgList = ParseArriveOpenClose(string)
        return msgList[1]
    if kind==3 or kind == 4:
        msgList = ParseInOut(string)
        return msgList[2]

def PraserElvId(string):
    mgList = string.split("-")
    return mgList[-1]

def ClassifyIntoBuilding(string,elvList): #将输出分类到不同楼座的输出
    elvId = PraserElvId(string)
    for i in elvList:
        if i.elvId == elvId:
            i.addMessage(string)
            break


def checkLogy(outPutList, elevator): #检查逻辑
    i = 0
    flag = True
    if len(outPutList)==0: return True
    firstString = outPutList[0]
    firstKind=ParseKind(firstString)
    if firstKind!=0 and firstKind!=1:#第一条必定是open或arrive
        print("[WrongLogy]")
        print("FirstString:"+firstString)
        return False
    while i < len(outPutList)-1:
        curString = outPutList[i]
        nextString = outPutList[i+1]
        curKind = ParseKind(curString)
        nextKind = ParseKind(nextString)
        if curKind==0:
            if nextKind==2 or nextKind==3 or nextKind==4:
                print("[Logical Error] Arrive之后逻辑错误")
                print("当前输出:"+curString)
                print("下一条输出:"+nextString)
                flag= False
        if curKind==1:
            if nextKind==0 or nextKind==1:
                print("[Logical Error] Open之后逻辑错误")
                print("当前输出:"+curString)
                print("下一输出:"+nextString)
                flag= False
        if curKind==2:
            if nextKind==2 or nextKind==3 or nextKind==4:
                print("[Logical Error] Close之后逻辑错误")
                print("当前输出:"+curString)
                print("下一输出:"+nextString)
                flag= False
        if curKind==3:
            if nextKind==0 or nextKind==1:
                print("[Logical Error] In之后逻辑错误")
                print("当前输出:"+curString)
                print("下一输出:"+nextString)
                flag= False
        if curKind==4:
            if nextKind==0 or nextKind==1:
                print("[Logical Error] Out之后逻辑错误")
                print("当前输出:"+curString)
                print("下一输出:"+nextString)
                flag= False
        i = i + 1
    lastString = outPutList[-1]
    lastKind = ParseKind(lastString)
    if lastKind==1 or lastKind==3 or lastKind==4:
        print("[Logical Error] 最后一条输出逻辑错误")
        print("最后输出:"+lastString)
    #print("[Logy Accepted]")
    return flag

def ParseReq(Req):
    string = RemoveTime(Req)
    msg = string.split("-")
    id = int(msg[0])
    building = msg[2]
    fromFloor = int(msg[3])
    toFloor = int(msg[6])
    return (id,building,fromFloor,toFloor)


def checkPerson(outPutList,elevator):
    flag = True
    inList = []
    outList = []
    floors =[[] for i in range(0,10)]
    for string in outPutList:
        kind = ParseKind(string)
        if kind==3:#in
            msg = ParseInOut(string)
            pId = msg[1]
            find = False
            # for req in nameList:
            #     if req[0]==pId:
            #         find = True
            #         break
            # if not find:## 确认是有这个请求的人近来
            #     print("[Person Error] 进入了一个没有请求的人")
            #     print("当前输出:"+string)
            #     flag= False
            block = msg[2]
            floor = msg[3]
            building = msg[2]
            if elevator.type == "floor":
                if (elevator.mask >> (ord(block) - ord("A"))) & 1 != 1:
                    print("横向电梯" + str(elevator.elvId) + "不能在" + block + "开门")
                    flag = False
            Req=(pId,building,floor)
            inList.append(Req) ##进入电梯
            if len(inList) > elevator.ca:
                print("[Person Error] 电梯超载")
                print("当前输出:"+string)
                flag= False
        if kind==4:#Out
            msg = ParseInOut(string)
            pId = msg[1]
            floor = msg[3]
            block = msg[2]
            find = False
            for i in range(0,len(inList)):
                if inList[i][0]==pId:
                    Req = inList[i]
                    inList.remove(Req)
                    toFloor = msg[3]
                    Req = Req+tuple([toFloor])
                    outList.append(Req)#离开电梯
                    find = True
                    break
            if not find:
                print("[Person Error] 一个本不在电梯中的人出去了")
                print("当前输出:"+string)
                flag= False
            if elevator.type == "floor":
                if (elevator.mask >> (ord(block) - ord("A"))) & 1 != 1:
                    print("横向电梯" + str(elevator.elvId) + "不能在" + block + "开门")
                    flag = False
    outSet = set(outList)
    # nameSet = set(nameList)
    if len(inList)!=0:
        print("[Person Error] 运行结束时还有人在电梯内")
        for req in inList:
            print("PersonID:"+str(req[0]))
        flag= False
    return flag

def checkPosition(outPutList):
    flag=True
    pos=1
    for string in outPutList:
        kind = ParseKind(string)
        if kind==0:
            msg = ParseArriveOpenClose(string)
            curPos = msg[2]
            if (curPos-pos != 1) and (curPos-pos != -1):
                print("[Position Error] 电梯一次移动超过一层或移到相同楼层")
                print("当前输出:"+string)
                flag= False
            pos = curPos
        if kind==1 or kind ==2 :
            msg = ParseArriveOpenClose(string)
            curPos = msg[2]
            if curPos!=pos:
                print("[Position Error] 在一个未到达楼层开关门")
                print("当前输出:"+string)
                flag= False
        if kind==3 or kind == 4:
            msg = ParseInOut(string)
            curPos = msg[3]
            if curPos!=pos:
                print("[Position Error] 在一个未到达楼层进出人员")
                print("当前输出:"+string)
                flag= False
        if pos < 1 or pos > 10:
            print("[Position Error] 电梯运行到一个错误位置")
            print("当前输出:"+string)
            flag= False
    #print("[Position Accepted]")
    return flag

def checkPosition2(outPutList):
    flag=True
    pos='A'
    for string in outPutList:
        kind = ParseKind(string)
        if kind==0:
            msg = ParseArriveOpenClose(string)
            curPos = msg[1]
            if ((ord(pos)-ord(curPos) + 5) % 5 != 1) and ((ord(curPos)-ord(pos) + 5) % 5 != 1):
                print("[Position Error] 电梯一次移动超过一层或移到相同楼层")
                print("当前输出:"+string)
                flag= False
            pos = curPos
        if kind==1 or kind ==2 :
            msg = ParseArriveOpenClose(string)
            curPos = msg[1]
            if curPos!=pos:
                print("[Position Error] 在一个未到达楼洞开关门")
                print("当前输出:"+string)
                flag= False
        if kind==3 or kind == 4:
            msg = ParseInOut(string)
            curPos = msg[2]
            if curPos!=pos:
                print("[Position Error] 在一个未到达楼栋进出人员")
                print("当前输出:"+string)
                flag= False
        if ord(pos) < ord('A') or ord(pos) > ord('E'):
            print("[Position Error] 电梯运行到一个错误位置")
            print("当前输出:"+string)
            flag= False
    #print("[Position Accepted]")
    return flag

def checkTime(outPutList, elevator):
    curTime = 0
    nextTime = 0
    flag = True
    for string in outPutList:
        nextTime = ParseTime(string)
        if nextTime < curTime:
            print("[Time Error] 时间戳非递增")
            print("当前输出:"+string)
            flag=False
        curTime = nextTime
    #print("[Time Sequence Accepted]")
    for i in range(0,len(outPutList)): #检查Arrive的0.4s
        if i == 0: continue
        curString = outPutList[i]
        lastString = outPutList[i-1]
        curKind = ParseKind(curString)
        if curKind==0:
            lastTime = ParseTime(lastString)
            curTime = ParseTime(curString)
            if round(curTime - lastTime,4) < elevator.speed:
                print("[Time Error] 电梯移动过快")
                print("上一输出:"+lastString)
                print("当前输出:"+curString)
                flag= False
    #print("[Arrive Time Accepted]")
    openTime = 0
    closeTime = 0
    for string in outPutList:
        kind = ParseKind(string)
        if kind==1:
            openTime = ParseTime(string)
        if kind==2:
            closeTime = ParseTime(string)
            if round(closeTime - openTime,4) <0.4000:
                print("[Time Error] 电梯开关门过快")
                print("开门时间戳:"+str(openTime))
                print("关门时间戳:"+str(closeTime))
                flag= False
    #print("[Open and Close Time Accepted]")
    return flag

def getElvList(testcase):
    elvList = []
    elvList.append(Elevator("[0.0]ADD-building-1-A-8-0.6"))
    elvList.append(Elevator("[0.0]ADD-building-2-B-8-0.6"))
    elvList.append(Elevator("[0.0]ADD-building-3-C-8-0.6"))
    elvList.append(Elevator("[0.0]ADD-building-4-D-8-0.6"))
    elvList.append(Elevator("[0.0]ADD-building-5-E-8-0.6"))
    elvList.append(Elevator("[0.0]ADD-floor-6-1-8-0.6-31"))
    for string in testcase:
        string = RemoveTime(string)
        mgList = string.split("-")
        if mgList[0] == "ADD":
            elvList.append(Elevator("[0.0]" + string))
    return elvList

def checkReq(testcase, output):
    inlist = []
    peopleList = []
    outlist = []
    target = []
    for string in testcase:
        temp = RemoveTime(string).split("-")
        string = RemoveTime(string)
        if temp[0] != "ADD":
            target.append(temp)
            peopleList.append(Person(string))
    for string in output:
        temp = RemoveTime(string).split("-")
        string = RemoveTime(string)
        id = temp[1]
        if temp[0] == "IN":
            for person in peopleList:
                if person.id == id:
                    person.message.append(string)
                    break
        if temp[0] == "OUT":
            for person in peopleList:
                if person.id == id:
                    person.message.append(string)
                    break
    for person in peopleList:
        status = "out"
        for message in person.message:
            temp = message.split("-")
            if temp[0] == "IN":
                if status != "out":
                    print("进了又进")
                    return False
                status = "in"
                if person.nowFloor != temp[3] or person.nowBlock != temp[2]:
                    print("乘客没在当前层上电梯")
                    return False
            if temp[0] == "OUT":
                if status != "in":
                    print("出了又出")
                    return False
                status = "out"
                person.setNow(message)
        if status != "out" or person.nowFloor != person.finalFloor or person.nowBlock != person.finalBlock:
            print("没到地方")
            return False
    totalFlag = True

    return totalFlag


def check(time):
    dataCheckLoc = "./"
    # dataCheckCommand = "./datacheck1.exe"
    # dataCheckSp = subprocess.Popen(dataCheckCommand,cwd=dataCheckLoc,stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    # stdout = dataCheckSp.communicate()
    # timeString = bytes.decode(stdout[0])
    # base = float(timeString.split(" ")[6].split(",")[0])
    # max = float(timeString.split(" ")[9])
    f = open("stdin.txt")
    fout = open("stdout.txt","r")
    totalOutPut = []
    for string in fout.readlines():
        if string[0]!="[": break
        totalOutPut.append(RemoveEnter(string))
    fin = open("stdin.txt", "r")
    testCase = []
    for string in fin.readlines():
        testCase.append(RemoveEnter(string))
    # totalNameList = getReqNameList(testCase=testCase)
    elvList = getElvList(testCase)
    for string in totalOutPut:
        ClassifyIntoBuilding(string,elvList)
    fout.close()
    # fin = open("stdin.txt","r")
    # testCase = []
    # for string in fin.readlines():
    #     testCase.append(RemoveEnter(string))
    # totalNameList = getReqNameList(testCase=testCase)
    totalFlag = True
    for elevator in elvList:
        subFlag = True
        building="E"
        outPutList = elevator.message
        if not checkLogy(outPutList=outPutList, elevator=elevator):
            totalFlag=False
            subFlag=False
        if not checkPerson(outPutList,elevator = elevator):
            totalFlag=False
            subFlag=False
        if elevator.type == "floor":
            if not checkPosition2(outPutList):
                totalFlag=False
                subFlag=False
        else:
            if not checkPosition(outPutList):
                totalFlag=False
                subFlag=False
        if not checkTime(outPutList=outPutList, elevator= elevator):
            totalFlag=False
            subFlag=False
        if subFlag:
            print("Elevator "+elevator.elvId+": Accepted")
        else:
            print("Elevator "+elevator.elvId+": Wrong Answer")
        print()
    if not checkReq(testCase, totalOutPut):
        totalFlag = False
    if time > 210:
        totalFlag = False
    if totalFlag:
        print("Result: Accepted")
        print(time)
    else:
        print("Result: Wrong Answer")
        print(time)
    return totalFlag

#check()