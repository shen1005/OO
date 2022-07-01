from cgi import test
import random
from unittest import TestCase
def getTimeStamps(num):
    times = []
    curtime = 1.0
    for i in range(0,num):
        curtime = curtime + random.uniform(0.0,0.5)
        curtime = round(curtime,1)
        times.append(curtime)
    return times

def getPersonReq(id,fromFloor,toFloor, frombuilding, tobuilding):
    string = str(id) + "-FROM-"+frombuilding+"-"+str(fromFloor)+"-TO-"+tobuilding+"-"+str(toFloor)
    return string

# def getPersonReq2(id,frombuilding,tobuilding,toFloor):
#     string = str(id) + "-FROM-"+frombuilding+"-"+str(toFloor)+"-TO-"+tobuilding+"-"+str(toFloor)
#     return string

def getElvReq(id, type, building, floor, ca, speed, mask):
    if type == "building":
        string = "ADD-building-" + str(id) + "-" + building + "-" + str(ca) + "-" + str(speed)
    else:
        string = "ADD-floor-" + str(id) + "-" + str(floor) + "-" + str(ca) + "-" + str(speed) + "-" + str(mask)
    return string

def mergeTimeAndReq(time,req):
    string = "["+str(time)+"]"+req
    return string

def getTestCase(num):
    times = getTimeStamps(num)
    testcase = []
    buildings = ["A","B","C","D","E"]
    speeds = [0.2, 0.4, 0.6]
    cas = [4, 6, 8]
    ids = set()
    while len(ids) < num:
        ids.add(random.randint(7,9999))
    ids = list(ids)
    elv_num = 6
    masks = range(0, 32)
    for i in range(0,num):
        temp = random.randint(0, 99)
        if temp < 50 and elv_num < 15:
            floor = random.randint(1, 10)
            building = buildings[random.randint(0, len(buildings) - 1)]
            if temp % 2 == 0:
                type = "building"
                mask = 31
            else:
                type = "floor"
                mask = masks[random.randint(0, len(masks) - 1)]
            id = ids[i]
            ca = cas[random.randint(0, len(cas) - 1)]
            speed = speeds[random.randint(0, len(speeds) - 1)]
            req = getElvReq(id,type, building, floor, ca, speed, mask)
            time = times[i]
            elv_num += 1
            testcase.append((mergeTimeAndReq(time, req)))
        else:
                building1 = buildings[random.randint(0,len(buildings)-1)]
                building2 = buildings[random.randint(0, len(buildings) - 1)]
                fromFloor = random.randint(1,10)
                toFloor = random.randint(1,10)
                while toFloor==fromFloor and building1 == building2:
                    toFloor = random.randint(1,10) ## 避免to和from相等
                id = ids[i]
                req = getPersonReq(id,fromFloor,toFloor, building1, building2)
                time = times[i]
                testcase.append(mergeTimeAndReq(time,req))
    return testcase



