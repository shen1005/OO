from email import message
from random import choice, randint, random
from tkinter import W

# instrs = ['ap', 'ap', 'ar',
#           'qci', 'qci', 'ag', 'atg', 'ag', 'atg', 'ag', 'atg',
#           'qgps', 'qgrs', 'qgvs',
#           'qgcs', 'qgam', 'qgav',
#           'qasu', 'bf', 'qm',
#           'qmp', 'qmp', 'qmp', 'qmp',
#           'qsl', 'qsl',
#           'dfg', 'dfg',
#           'qbs', 'qbs'
#           ]
instrs = ['ap', 'ap', 'ar',
          'qci', 'qci', 'ag', 'atg', 'ag', 'atg', 'ag', 'atg',
          'qv', 'qv', 'qps', 'qps',
          'dfg', 'dfg',
          'qbs', 'qbs',
          'qgps','qgps',
          'qgvs','qgvs',
          'qgav','qgav',
          'am','am',
          'sm','sm',
          'qsv','qsv',
          'qrm','qrm',
          'qlc','qlc',
          'arem', 'arem',
          'anm', 'anm',
          'cn', 'cn',
          'aem','aem',
          'sei','sei',
          'qp', 'qp',
          'dce', 'dce',
            'qm', 'qm',
          'sim', 'sim'
          ]

name = ["Limbo", "Nyima", "Claw", "Orange"]
adj = ["fuck", "ugly", "damn", "shit", "nuts", "crap", "ass", "dick", "jerk", "cum", "cunt"]
cnt = 0

limit = {
    "ap" : 5000,
    "qci" : 333,
    "ag":25,
    "qlc": 100,
    "sim":1000
}

instr_count = {
    "ap" : 0,
    "qci": 0,
    "ag" : 0,
    "qlc": 0,
    "sim": 0
}


def r(people_num):
    return randint(1, people_num + 2)  # +1 allow for exception

def f(emoji_num):
    return randint(1, emoji_num + 2)

def generate(LENGTH, PEOPLE):
    global cnt
    instr_count["ap"] = 0
    instr_count["qci"] = 0
    instr_count["ag"] = 0
    instr_count["qlc"] = 0
    print(choice(adj) + choice(name) + " is coming...")
    cnt += 1
    if random() < 0.8:
        print("GENERATE ADD FIRST")
        return generate_add_first(LENGTH, PEOPLE)
    else:
        print("GENERATE RANDOM")
        return generate_random(LENGTH, PEOPLE)


def generate_n_square(LENGTH, PEOPLE):
    instrlist = ''
    people_num = PEOPLE
    group_num = 0

    for i in range(people_num):
        instrlist += "ap {} {} {} {}\n".format(i, choice(adj) + choice(name), randint(1000000, 10000000),
                                               randint(1, 80))
    relations = randint(people_num, people_num*(people_num)/2)
    relations = min(LENGTH - 2*people_num, relations)
    for i in range(relations):
        instrlist += "ar {} {} {}\n".format(r(people_num), r(people_num), randint(1, 1000))

    for i in range(LENGTH - PEOPLE - relations):
        pass

    return instrlist


def generate_add_first(LENGTH, PEOPLE):
    instrlist = ''
    people_num = PEOPLE
    group_num = 0
    message_num = 0
    emoji_num = 0
    for i in range(people_num):
        instrlist += "ap {} {} {}\n".format(i, choice(adj) + choice(name), randint(0, 200))
    relations = randint(int(people_num/2), min(people_num*10, int(LENGTH/2)))
    for i in range(relations):
        instrlist += "ar {} {} {}\n".format(r(people_num), r(people_num), randint(0, 1000))
    for i in range(50):
        instrlist += "sei {}\n".format(emoji_num + 1)
        emoji_num += 1
    for i in range(LENGTH - people_num - relations):
        instr = choice(instrs)
        if instr in limit.keys():
            if instr_count[instr] > limit[instr]:
                i -= 1
                continue
            else:
                instr_count[instr] += 1
        if instr == 'ap':
            instrlist += "ap {} {} {}\n".format(people_num + i, choice(adj) + choice(name),
                                                   randint(0, 80))
        elif instr in ['ar']:
            instrlist += "{} {} {} {}\n".format(instr, r(people_num), r(people_num), randint(0, 1000))
        elif instr in ['qci', 'qv']:
            instrlist += '{} {} {}\n'.format(instr, r(people_num), r(people_num))
        elif instr == 'ag':
            instrlist += 'ag {}\n'.format(group_num + 1)
            group_num += 1
        elif instr in ['atg', 'dfg']:
            instrlist += '{} {} {}\n'.format(instr, r(people_num), randint(0, group_num))
        # elif instr == 'qasu':
        #     instrlist += 'qasu {} {}\n'.format(randint(0, 20), randint(0, 80))
        elif instr == 'qbs':
            instrlist += 'qbs\n'
        elif instr == 'qps':
            instrlist += 'qps\n'
        # elif instr == 'qm':
        #     instrlist += 'qm {}\n'.format(r(people_num))
        elif instr in ['qgps', 'qgav', 'qgvs']:
             instrlist += '{} {}\n'.format(instr, randint(0, group_num))
        # elif instr in ['qgcs', 'qgam', 'qgav']:
        #     instrlist += '{} {}\n'.format(instr, randint(0, group_num))
        elif instr in ['am', 'arem', 'anm']:
            temp = randint(0,1)
            if (temp == 0):
                instrlist += '{} {} {} {} {} {}\n'.format(instr, message_num + 1, r(1000), temp, r(people_num), r(people_num))
            else:
                instrlist += '{} {} {} {} {} {}\n'.format(instr, message_num + 1, r(1000), temp, r(people_num), r(group_num))
            message_num += 1
        elif instr in ['aem']:
            temp = randint(0,1)
            if (temp == 0):
                instrlist += 'aem {} {} {} {} {}\n'.format(message_num + 1, r(100), temp, r(people_num), r(people_num))
            else:
                instrlist += 'aem {} {} {} {} {}\n'.format(message_num + 1, r(100), temp, r(people_num), r(group_num))
            message_num += 1
        elif instr in ['sm', 'sim']:
            instrlist += '{} {}\n'.format(instr, randint(0, message_num + 2))
        elif instr in ['sei', 'dce']:
            instrlist += '{} {}\n'.format(instr, randint(0, 100))
        elif instr in ['qp', 'dce']:
            instrlist += '{} {}\n'.format(instr, randint(0, 100))
        elif instr in ['qsv','qrm','qlc', "cn", 'qm']:
            instrlist += '{} {}\n'.format(instr, r(people_num))
        else:
            print("unimplemented instruction {}".format(instr))
            exit(1)
    #print(len(instrlist.split("\n")))
    return instrlist


def generate_random(LENGTH, PEOPLE):
    instrlist = ''
    people_num = PEOPLE
    group_num = 0
    message_num = 0
    for i in range(LENGTH):
        instr = choice(instrs)
        if instr in limit.keys():
            if instr_count[instr] > limit[instr]:
                i -= 1
                continue
            else:
                instr_count[instr] += 1
        if instr == 'ap':
            instrlist += "ap {} {} {}\n".format(people_num + i, choice(adj) + choice(name),
                                                randint(0, 80))
        elif instr in ['ar']:
            instrlist += "{} {} {} {}\n".format(instr, r(people_num), r(people_num), randint(0, 1000))
        elif instr in ['qci', 'qv']:
            instrlist += '{} {} {}\n'.format(instr, r(people_num), r(people_num))
        elif instr == 'ag':
            instrlist += 'ag {}\n'.format(group_num + 1)
            group_num += 1
        elif instr in ['atg', 'dfg']:
            instrlist += '{} {} {}\n'.format(instr, r(people_num), randint(0, group_num))
        # elif instr == 'qasu':
        #     instrlist += 'qasu {} {}\n'.format(randint(0, 20), randint(0, 80))
        elif instr == 'qbs':
            instrlist += 'qbs\n'
        elif instr == 'qps':
            instrlist += 'qps\n'
        # elif instr == 'qm':
        #     instrlist += 'qm {}\n'.format(r(people_num))
        elif instr in ['qgps', 'qgav', 'qgvs']:
            instrlist += '{} {}\n'.format(instr, randint(0, group_num))
        # elif instr in ['qgcs', 'qgam', 'qgav']:
        #     instrlist += '{} {}\n'.format(instr, randint(0, group_num))
        elif instr in ['am', 'arem', 'anm']:
            temp = randint(0, 1)
            if (temp == 0):
                instrlist += '{} {} {} {} {} {}\n'.format(instr, message_num + 1, r(1000), temp, r(people_num),
                                                          r(people_num))
            else:
                instrlist += '{} {} {} {} {} {}\n'.format(instr, message_num + 1, r(1000), temp, r(people_num),
                                                          r(group_num))
            message_num += 1
        elif instr in ['aem']:
            temp = randint(0, 1)
            if (temp == 0):
                instrlist += 'aem {} {} {} {} {}\n'.format(message_num + 1, r(100), temp, r(people_num),
                                                           r(people_num))
            else:
                instrlist += 'aem {} {} {} {} {}\n'.format(message_num + 1, r(100), temp, r(people_num),
                                                           r(group_num))
            message_num += 1
        elif instr in ['sm', 'sim']:
            instrlist += '{} {}\n'.format(instr, randint(0, message_num + 2))
        elif instr in ['sei', 'dce']:
            instrlist += '{} {}\n'.format(instr, randint(0, 100))
        elif instr in ['qp', 'dce']:
            instrlist += '{} {}\n'.format(instr, randint(0, 100))
        elif instr in ['qsv', 'qrm', 'qlc', "cn", 'qm']:
            instrlist += '{} {}\n'.format(instr, r(people_num))
        else:
            print("unimplemented instruction {}".format(instr))
            exit(1)
            # print(len(instrlist.split("\n")))
    return instrlist


if __name__ == '__main__':
    f = open("data_generate.txt", "w")
    print(generate(1000, 200))