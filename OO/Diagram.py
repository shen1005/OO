import json
import random
IN_TYPE = ["byte", "short", "int", "long", "float", "double", "char", "boolean", "String"]
OUT_TYPE = ["byte", "short", "int", "long", "float", "double", "char", "boolean", "String", "void"]
VISIBILITY = ["public", "private", "protected", "package"]


def randomBool():
    return random.choice([0, 1]) == 0


class State:
    def __init__(self, id:int, name:str, parent_id:int, is_init:bool, is_final:bool):
        self.id = id
        self.name = name
        self.parent_id = parent_id
        self.is_init = is_init
        self.is_final = is_final
        self.name = name

    def getJson(self):
        type = "UML"
        if self.is_init:
            type = type + "Pseudostate"
        elif self.is_final:
            type = type + "FinalState"
        else:
            type = type + "State"
        st_js = {"_parent": str(self.parent_id),
                 "visibility": random.choice(VISIBILITY),
                 "name": self.name,
                 "_type": type,
                 "_id": str(self.id)}
        return json.dumps(st_js)


class Transition:
    def __init__(self, id, parent_id, states:list = None, source:State = None, target:State = None):
        self.id = id
        self.parent_id = parent_id
        self.states = states
        self.source = source
        self.target = target
        if self.source is None:
            self.source = random.choice(self.states)
            while self.source.is_final:
                self.source = random.choice(self.states)
        if self.target is None:
            self.target = random.choice(self.states)
            while self.target.is_init:
                self.target = random.choice(self.states)

    def getJson(self):
        name = "Transition" + str(self.id)
        tr_js = {"_parent": str(self.parent_id),
                 "visibility": random.choice(VISIBILITY),
                 "guard": None,
                 "name": name,
                 "_type":"UMLTransition",
                 "_id": str(self.id),
                 "source": str(self.source.id),
                 "target": str(self.target.id)}
        return json.dumps(tr_js)


class StateMachine:
    def __init__(self, id, name):
        self.id = id
        self.ids = id
        self.region = None
        self.name = name

    def getElements(self):
        elements = []
        sm_js = {"_type": "UMLStateMachine",
                 "_parent": "0",
                 "_id": str(self.id),
                 "name": self.name}
        elements.append(json.dumps(sm_js))
        id = self.ids = self.ids + 1
        rg = Region(id, self.id)
        self.region = rg
        elements.extend(rg.getElements())
        self.ids = rg.ids
        return elements

    def getInstrs(self):
        instrs = [self.getStateCount()]
        for state in self.region.states:
            instrs.append(self.getStateIsCriticalPoint(state))
        for _ in range(10):
            instrs.append(self.getStateIsCriticalPoint(State(0, "test", 0, False, False)))
        for transition in self.region.transitions:
            instrs.append(self.getTransitionTrigger(transition.source, transition.target))
        for _ in range(10):
            instrs.append(self.getTransitionTrigger(random.choice(self.region.states), random.choice(self.region.states)))
        return instrs

    def getStateCount(self):
        return "STATE_COUNT " + self.name

    def getStateIsCriticalPoint(self, state:State):
        return "STATE_IS_CRITICAL_POINT " + self.name + " " + state.name

    def getTransitionTrigger(self, source:State, target:State):
        return "TRANSITION_TRIGGER " + self.name + " " + source.name + " " + target.name


class Region:
    def __init__(self, id, parent_id):
        self.id = id
        self.ids = id
        self.parent_id = parent_id
        self.states = []
        self.final_state_num = 10
        self.state_num = 30
        self.transition_num = 100
        self.transitions = []
        self.event_num = 200

    def getElements(self):
        elements = []
        name = "Region" + str(self.id)
        rg_js = {"_parent": str(self.parent_id),
                 "visibility": random.choice(VISIBILITY),
                 "name": name,
                 "_type": "UMLRegion",
                 "_id": str(self.id)}
        elements.append(json.dumps(rg_js))
        self.ids = self.ids + 1
        name = "PseudoState" + str(self.ids)
        pseudostate = State(self.ids, name, self.id, True, False)
        self.states.append(pseudostate)
        elements.append(pseudostate.getJson())

        graph_type = random.choice(["tree", "chain", "random"])
        if graph_type == "random":
            for i in range(self.final_state_num):
                self.ids = self.ids + 1
                name = "FinalState" + str(self.ids)
                final_state = State(self.ids, name, self.id, False, True)
                self.states.append(final_state)
                elements.append(final_state.getJson())
            for i in range(self.state_num):
                self.ids = self.ids + 1
                name = "State" + str(self.ids)
                state = State(self.ids, name, self.id, False, False)
                self.states.append(state)
                elements.append(state.getJson())
            for i in range(self.transition_num):
                self.ids = self.ids + 1
                transition = Transition(self.ids, self.id, states=self.states)
                self.transitions.append(transition)
                elements.append(transition.getJson())
        elif graph_type == "tree":
            for i in range(self.state_num):
                self.ids = self.ids + 1
                name = "State" + str(self.ids)
                state = State(self.ids, name, self.id, False, False)
                self.states.append(state)
                elements.append(state.getJson())
            for i in range(self.final_state_num):
                self.ids = self.ids + 1
                name = "FinalState" + str(self.ids)
                final_state = State(self.ids, name, self.id, False, True)
                self.states.append(final_state)
                elements.append(final_state.getJson())
            for i in range(1, len(self.states)):
                self.ids = self.ids + 1
                fa = random.choice(self.states[0:i])
                transition = Transition(self.ids, self.id, source=fa, target=self.states[i])
                self.transitions.append(transition)
                elements.append(transition.getJson())
        else:
            for i in range(self.state_num):
                self.ids = self.ids + 1
                if i <= 15 or randomBool():
                    name = "State" + str(self.ids)
                else:
                    name = random.choice(self.states).name
                state = State(self.ids, name, self.id, False, False)
                self.states.append(state)
                elements.append(state.getJson())
            for i in range(self.final_state_num):
                self.ids = self.ids + 1
                name = "FinalState" + str(self.ids)
                final_state = State(self.ids, name, self.id, False, True)
                self.states.append(final_state)
                elements.append(final_state.getJson())
            for i in range(1, len(self.states)):
                self.ids = self.ids + 1
                transition = Transition(self.ids, self.id, source=self.states[i - 1], target=self.states[i])
                self.transitions.append(transition)
                elements.append(transition.getJson())

        for i in range(self.event_num):
            self.ids = self.ids + 1
            event = Event(self.ids, self.transitions)
            elements.append(event.getJson())
        return elements


class Event:
    def __init__(self, id, transitions:list):
        self.id = id
        self.transition = random.choice(transitions)

    def getJson(self):
        name = "Event" + str(self.id)
        ev_js = {"_parent": str(self.transition.id),
                 "expression": None,
                 "visibility": random.choice(VISIBILITY),
                 "name": name,
                 "_type": "UMLEvent",
                 "_id": str(self.id),
                 "value": None}
        return json.dumps(ev_js)


class Message:
    def __init__(self, id, name, parent_id, participants:list):
        self.id = id
        self.name = name
        self.parent_id = parent_id
        self.is_create = randomBool()
        if self.is_create:
            self.source = random.choice(participants)
            while self.source.is_end_point:
                self.source = random.choice(participants)
            self.target = random.choice(participants)
            while self.target.is_end_point:
                self.target = random.choice(participants)
        else:
            self.source = random.choice(participants)
            self.target = random.choice(participants)
            while self.source.is_end_point and self.target.is_end_point:
                self.source = random.choice(participants)
                self.target = random.choice(participants)

    def getJson(self):
        if self.is_create:
            ret = {"messageSort": "createMessage",
                   "_parent": str(self.parent_id),
                   "visibility": random.choice(VISIBILITY),
                   "name": self.name,
                   "_type": "UMLMessage",
                   "_id": str(self.id),
                   "source": str(self.source.id),
                   "target": str(self.target.id)}
        else:
            ret = {"messageSort": "synchCall",
                   "_parent": str(self.parent_id),
                   "visibility": random.choice(VISIBILITY),
                   "name": self.name,
                   "_type":"UMLMessage",
                   "_id": str(self.id),
                   "source": str(self.source.id),
                   "target": str(self.target.id)}
        return json.dumps(ret)


class Participant:
    def __init__(self, id:int, name:str, parent_id:int, is_end_point:bool):
        self.id = id
        self.name = name
        self.is_end_point = is_end_point
        self.parent_id = parent_id

    def getJson(self):
        if self.is_end_point:
            ret = {"_parent": str(self.parent_id),
                   "visibility": random.choice(VISIBILITY),
                   "name": self.name,
                   "_type": "UMLEndpoint",
                   "_id": str(id)}
        else:
            ret = {"_parent": str(self.parent_id),
                   "visibility": random.choice(VISIBILITY),
                   "name": self.name,
                   "_type": "UMLLifeline",
                   "isMultiInstance": False,
                   "_id": str(self.id),
                   "represent": "represent"}
        return json.dumps(ret)


class Interaction:
    def __init__(self, id:int, name:str):
        self.id = id
        self.name = name
        self.ids = id
        self.participants = []
        self.lifeline_num = 100
        self.end_point_num = 40
        self.message_num = 300

    def getElements(self):
        elements = []
        it_js = {"_parent": "0",
                 "visibility": random.choice(VISIBILITY),
                 "name": self.name,
                 "_type": "UMLInteraction",
                 "_id": str(self.id)}
        elements.append(json.dumps(it_js))
        for _ in range(self.lifeline_num):
            self.ids = self.ids + 1
            if len(self.participants) <= 6 or randomBool():
                name = "Lifeline" + str(self.ids)
            else:
                name = random.choice(self.participants).name
            lifeline = Participant(self.ids, name, self.id, False)
            self.participants.append(lifeline)
            elements.append(lifeline.getJson())
        for _ in range(self.end_point_num):
            self.ids = self.ids + 1
            name = "EndPoint" + str(self.ids)
            end_point = Participant(self.ids, name, self.id, True)
            self.participants.append(end_point)
            elements.append(end_point.getJson())
        for _ in range(self.message_num):
            self.ids = self.ids + 1
            name = "Message" + str(self.ids)
            elements.append(Message(self.ids, name, self.id, self.participants).getJson())
        return elements

    def getInstrs(self):
        instrs = [self.getParticipantCount()]
        for lifeline in self.participants:
            if lifeline.is_end_point:
                continue
            instrs.append(self.getParticipantCreator(lifeline.name))
        for _ in range(10):
            instrs.append(self.getParticipantCreator("test"))
        for lifeline in self.participants:
            if lifeline.is_end_point:
                continue
            instrs.append(self.getParticipantLostAndFound(lifeline.name))
        for _ in range(10):
            instrs.append(self.getParticipantLostAndFound("test"))
        return instrs

    def getParticipantCount(self):
        return "PTCP_OBJ_COUNT " + self.name

    def getParticipantCreator(self, name):
        return "PTCP_CREATOR " + self.name + " " + name

    def getParticipantLostAndFound(self, name):
        return "PTCP_LOST_AND_FOUND " + self.name + " " + name


class Diagram:

    def __init__(self):
        self.state_machine_num = 10
        self.state_machines = []

        self.interaction_num = 10
        self.interactions = []

        self.ids = 0

    def getData(self):
        elements, instrs = self.gets()
        ret = []
        ret.extend(elements)
        ret.append("END_OF_MODEL")
        ret.extend(instrs)
        return ret

    def gets(self):
        elements = []
        instrs = []
        for i in range(self.state_machine_num):
            element, instr = self.addStateMachine()
            elements.extend(element)
            instrs.extend(instr)
        _, instr = self.addStateMachine(is_not_found=True)
        instrs.extend(instr)
        for i in range(self.interaction_num):
            element, instr = self.addInteraction()
            elements.extend(element)
            instrs.extend(instr)
        _, instr = self.addInteraction(is_not_found=True)
        instrs.extend(instr)
        return elements, instrs

    def addStateMachine(self, is_not_found=False):
        elements = []
        id = self.ids = self.ids + 1
        if is_not_found or len(self.state_machines) <= 6 or randomBool():
            name = "StateMachine" + str(id)
        else:
            name = str(random.choice(self.state_machines).name)
        sm = StateMachine(id, name)
        self.state_machines.append(sm)
        elements.extend(sm.getElements())
        self.ids = sm.ids
        return elements, sm.getInstrs()

    def addInteraction(self, is_not_found=False):
        elements = []
        id = self.ids = self.ids + 1
        if is_not_found or len(self.interactions) <= 6 or randomBool():
            name = "Interaction" + str(id)
        else:
            name = random.choice(self.interactions).name
        it = Interaction(id, name)
        self.interactions.append(it)
        elements.extend(it.getElements())
        self.ids = it.ids
        return elements, it.getInstrs()
