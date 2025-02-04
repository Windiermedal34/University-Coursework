from graphics import *

def coursework():
    patchsize = int(input("enter the height/width of patches: "))
    win = GraphWin("Patches", patchsize*100, patchsize*100)
    patchsize = patchsize
    while True:
        colours = ["red","green","blue","orange","yellow","black","cyan","magenta","grey"]
        colour1 = input("Enter a colour for colour 1: ")
        if colour1 not in colours:
            print("invalid")
            continue
        colour2 = input("Enter a colour for colour 2: ")
        if colour2 not in colours:
            print("invalid")
            continue
        colour3 = input("Enter a colour for colour 3: ")
        if colour1 not in colours:
            print("invalid")
            continue
        break

    for a in range(patchsize-2):
        for b in range(patchsize):
            x_extension = 100+b*200
            y_extension = ((patchsize-1)*100)-b*200+a*100
            colour = colour3
            penultPattern(win, patchsize, x_extension, y_extension, colour)
    for c in range(patchsize):
        for d in range(patchsize):
            x_extension = d*200
            y_extension = ((patchsize-2)*100)-d*200-c*100
            colour = colour1
            penultPattern(win, patchsize, x_extension, y_extension, colour)
    for e in range(patchsize-3):
            x_extension = 100+e*200
            y_extension = ((patchsize-2)*100)-e*200
            colour = colour2
            penultPattern(win, patchsize, x_extension, y_extension, colour)
    for f in range(patchsize):
        for g in range(patchsize):
            x_extension = 100+g*200
            y_extension = ((patchsize-2)*100)-g*200-f*100-100
            colour = colour1
            penultPattern(win, patchsize, x_extension, y_extension, colour)
    for l in range(2):
        for j in range(patchsize):
            for k in range(patchsize):
                for i in range(10):
                    extension1 = l*100
                    extension2 = j*200
                    extension3 = k*200
                    extension4 = i*10
                    colour = colour3
                    ultPattern(win, patchsize, colour, extension1, extension2, extension3, extension4)
    for l in range(2):
        for j in range(patchsize):
            for k in range(patchsize):
                for i in range(10):
                    extension1 = 0
                    extension2 = 0
                    extension3 = k*200
                    extension4 = i*10
                    colour = colour2
                    ultPattern(win, patchsize, colour, extension1, extension2, extension3, extension4)

def penultPattern(win, patchsize, x_extension, y_extension, colour):
    for i in range(3):
        for j in range(3):
            square3a = Rectangle(Point(0+i*40+x_extension, 0+j*40+y_extension), Point(20+i*40+x_extension, 20+j*40+y_extension))
            square3a.setFill(colour)
            square3a.setOutline(colour)
            square3a.draw(win)
    for i in range(2):
        for j in range(3):
            square3b = Rectangle(Point(20+i*40+x_extension, 0+j*40+y_extension), Point(40+i*40+x_extension, 20+j*40+y_extension))
            square3b.setFill("white")
            square3b.setOutline("white")
            square3b.draw(win)
    for i in range(3):
        for j in range(2):
            square4a = Rectangle(Point(0+i*40+x_extension, 20+j*40+y_extension), Point(20+i*40+x_extension, 40+j*40+y_extension))
            square4a.setFill("white")
            square4a.setOutline("white")
            square4a.draw(win)
    for i in range(2):
        for j in range(2):
            square4b = Rectangle(Point(20+i*40+x_extension, 20+j*40+y_extension), Point(40+i*40+x_extension, 40+j*40+y_extension))
            square4b.setFill(colour)
            square4b.setOutline(colour)
            square4b.draw(win)
    for i in range(3):
        for j in range(3):
            for k in range(2):
                for l in range(2):
                    circle1b = Circle(Point(5+i*40+k*10+x_extension, 5+j*40+l*10+y_extension), 5)
                    circle1b.setFill("white")
                    circle1b.setOutline("white")
                    circle1b.draw(win)
    for i in range(2):
        for j in range(3):
            for k in range(2):
                for l in range(2):
                    circle2b = Circle(Point(25+i*40+k*10+x_extension, 5+j*40+l*10+y_extension), 5)
                    circle2b.setFill(colour)
                    circle2b.setOutline(colour)
                    circle2b.draw(win)
    for i in range(2):
        for j in range(2):
            for k in range(2):
                for l in range(2):
                    circle1b = Circle(Point(25+i*40+k*10+x_extension, 25+j*40+l*10+y_extension), 5)
                    circle1b.setFill("white")
                    circle1b.setOutline("white")
                    circle1b.draw(win)
    for i in range(3):
        for j in range(2):
            for k in range(2):
                for l in range(2):
                    circle1b = Circle(Point(5+i*40+k*10+x_extension, 25+j*40+l*10+y_extension), 5)
                    circle1b.setFill(colour)
                    circle1b.setOutline(colour)
                    circle1b.draw(win)

def ultPattern(win, patchsize, colour, extension1, extension2, extension3, extension4):
    line = Line(Point(0+extension2+extension3, ((patchsize-1)*100)-extension3+extension1), Point(10+extension2+extension3, (patchsize*100)-extension3+extension1))
    line.setOutline(colour)
    line.draw(win)
    line2 = Line(Point(0+extension2+extension3,((patchsize-1)*100)+extension4-extension3+extension1), Point(10+extension4+extension2+extension3,(patchsize*100)-extension3+extension1))
    line2.setOutline(colour)
    line2.draw(win)
    line3 = Line(Point(0+extension2+extension3, ((patchsize-1)*100)-extension3+extension1), Point(100+extension2+extension3, (((patchsize-1)*100)+10)-extension3+extension1))
    line3.setOutline(colour)
    line3.draw(win)
    line4 = Line(Point(0+extension4+extension2+extension3, ((patchsize-1)*100)-extension3+extension1), Point(0+100+extension2+extension3, (((patchsize-1)*100)+10)+extension4-extension3+extension1))
    line4.setOutline(colour)
    line4.draw(win)