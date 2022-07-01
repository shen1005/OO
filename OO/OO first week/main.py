import random
from xeger import Xeger
from sympy import *
index = 0
temp = ""
while len(temp) < 50:
    index = random.randint(0, 5)
    Term = ""
    while index > 0:
        factor3 = ""
        expr_factor = random.randint(1, 5)
        while expr_factor > 0:
            i = random.randint(0, 1)
            factor1 = Xeger().xeger("[+-]?[0-9]{1,3}")
            factor2 = Xeger().xeger("[+]?x(\*\*[0-3])?")
            factors = [factor1, factor2]
            factor3 += Xeger().xeger("[+-]") + factors[i]
            expr_factor -= 1
        factor3 = "(" + factor3 + ")"
        factor4 = factor3 + "**" + Xeger().xeger("\+?[0-3]")
        factor1 = Xeger().xeger("[+-]?[0-9]{0,3}")
        factor2 = Xeger().xeger("[+]?x(\*\*[0-8])?")
        factors = [factor1, factor2, factor3, factor4]
        i = random.randint(0, 3)
        Term += "*" + factors[i]
        index -= 1
    Term = Term.strip("*")
    temp += Xeger().xeger("[+-]") + Term
print(temp)
x = symbols('x')
f1 = symbols(temp)
print(str(simplify(f1)))

