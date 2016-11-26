'''A simple ui
'''

from graphics import *
import random
import time
import math
level = [["gray", "gray", "gray", "gray", "gray", "gray"],
 ["gray", "white", "white", "white", "white", "gray"],
 ["gray", "white", "white", "white", "white", "gray"],
 ["gray", "white", "white", "gray", "white", "gray"],
 ["gray", "white", "gray", "white", "white", "gray"],
 ["gray", "white", "white", "gray", "white", "gray"],
 ["gray", "gray", "white", "white", "gray", "gray"],
 ["gray", "white", "gray", "white", "white", "gray"],
 ["gray", "white", "white", "white", "gray", "gray"],
 ["gray", "white", "white", "gray", "white", "gray"],
 ["gray", "white", "white", "gray", "gray", "gray"],
 ["gray", "white", "gray", "white", "white", "gray"],
 ["gray", "gray", "white", "white", "white", "gray"],
 ["gray", "gray", "gray", "gray", "gray", "gray"]
]

class Minion():  
  def __init__(self, i, x,y, win): #
        self.i = i;
        self.x = x;
        self.y = y;
        self.oldx = x;
        self.oldy = y;
        self.dx = 0;
        self.dy = 0;
        self.color = "yellow";
         
        p = Point(self.x, self.y)
        self.body = Circle(p, 20) # set center and radius
        self.body.setFill(self.color) 
        self.body.draw(win)
        
        self.label = Text(p, str(i))
        self.label.draw(win)
        
        p2 = Point(self.x, self.y)
        self.head = Circle(p2, 5) # set center and radius
        self.head.setFill(self.color) 
        self.head.draw(win)
        
        self.angle = random.randint( 0, 10);
        self.size = 15
        hx = self.size * math.cos(self.angle)
        hy = self.size * math.sin(self.angle)
        self.head.move(hx, hy)

  def moveXY(self,x,y):  
        self.x +=x
        self.y +=y

  def update(self):
        x = self.x-self.oldx
        y = self.y-self.oldy
        self.body.move(x, y)
        self.head.move( x, y)
        self.label.move( x, y)

        self.oldx = self.x;
        self.oldy = self.y;
        
  def move(self):  
        x = 2*self.size * math.cos(self.angle)
        y = 2*self.size * math.sin(self.angle)
        self.moveXY( x, y)
        
  def turn(self, a):   
        hx = -self.size * math.cos(self.angle)
        hy = -self.size * math.sin(self.angle)
        
        self.angle += a * math.pi * 2 /8;
        
        hx += self.size * math.cos(self.angle)
        hy += self.size * math.sin(self.angle)
         
        self.head.move( hx, hy)
        
  def collision(self, other):
        c =  math.hypot(other.x - self.x, other.y - self.y) < self.size + other.size
        if c:
          self.dx = (self.x - other.x)/2
          self.dy = (self.y - other.y)/2
          return True
        if self.x < 80:
          self.dx = 80- self.x
          c =   True
        if self.y < 60:
          self.dy = 60- self.y
          c =   True
        if self.x > 280:
          self.dx = 280 - self.x
          c =   True
        if self.y > 580:
          self.dy = 580 - self.y
          c =   True
        return c
  def allign(self):
        self.moveXY(self.dx,self.dy)
        self.dx = 0;
        self.dy = 0;
           
     
def main():
    win = GraphWin('game', 380, 640) # give title and dimensions
    #win.yUp() # make right side up coordinates!

    #head = Circle(Point(40,100), 25) # set center and radius
    #head.setFill("yellow")
    #head.draw(win)
    
    message = Text(Point(win.getWidth()/2, 20), 'Click anywhere to move.')
    message.draw(win) 

    for y,line in enumerate(level):
        for x,c in enumerate(line):
          xx = 60+40 *x
          yy = 40+40 *y
          box = Rectangle(Point(xx, yy), Point(xx+40, yy+40)) # set corners of  
          box.setFill(c)
          box.draw(win)
    minions = [
      Minion( 1, random.randint(56, 125), random.randint(56, 265),win),
      Minion( 2, random.randint(26, 125), random.randint(56, 265),win),
      Minion( 3, random.randint(26, 125), random.randint(56, 265),win),
      Minion( 4, random.randint(26, 125), random.randint(56, 265),win),
      Minion( 5, random.randint(26, 125), random.randint(56, 265),win),
      Minion( 6, random.randint(56, 125), random.randint(56, 265),win),
      Minion( 7, random.randint(26, 125), random.randint(56, 265),win),
      Minion( 8, random.randint(26, 125), random.randint(56, 265),win),
      Minion( 9, random.randint(26, 125), random.randint(56, 265),win),
      Minion( 10, random.randint(26, 125), random.randint(56, 265),win),
    ]
    while(True):
      for m in minions:
        # if ai move is left then move = 1;
        move =  random.randint(-1, 1)
        m.turn(move);
        m.move()
      collision = True
      while collision:
        collision = False
        for m in minions: 
          for t in minions:
            if m is not t:
              c = m.collision(t)
              #print(c)
              if c:
                collision = True
        for m in minions: 
          m.allign()

      for m in minions:
        m.update()
      time.sleep(.2)
      print("----")
      #head = Circle(win.getMouse(), 5) # set center and radius
      #head.setFill("yellow")
      #head.draw(win) 
      win.flush() 
    win.close()
    

main()
