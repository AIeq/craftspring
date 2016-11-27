'''A simple ui
'''

from AI import Ai
from graphics import *
import random
import time
import math
import json
level = [["gray", "gray", "gray", "gray", "gray", "gray"],
 ["gray", "gray", "gray", "gray", "gray", "gray"],
 ["gray", "gray", "red", "red", "gray", "gray"],
 ["gray", "gray", "red", "red", "gray", "gray"],
 ["gray", "gray", "gray", "gray", "gray", "gray"],
 ["blue", "gray", "gray", "gray", "gray", "blue"],
 ["gray", "gray", "gray", "gray", "gray", "gray"],
 ["gray", "gray", "gray", "gray", "gray", "gray"],
 ["gray", "gray", "gray", "gray", "gray", "gray"],
 ["gray", "gray", "gray", "gray", "gray", "gray"],
 ["gray", "gray", "gray", "gray", "gray", "gray"],
 ["gray", "gray", "gray", "gray", "gray", "gray"],
 ["gray", "gray", "gray", "gray", "gray", "gray"],
 ["gray", "gray", "gray", "gray", "gray", "gray"]
]
ai = Ai()
class Minion():  
  def __init__(self, i,p, x,y, win): #
        self.i = i;
        self.x = x;
        self.y = y;
        self.oldx = x;
        self.oldy = y;
        self.dx = 0;
        self.dy = 0;
        #print(p)
        #self.color = json.loads(p).get("color");
        self.color = p
        
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
        
        self.angle = random.randint( 0, 7)  * math.pi * 2 /8
        self.size = 15
        hx = self.size * math.cos(self.angle)
        hy = self.size * math.sin(self.angle)
        self.head.move(hx, hy)

  def moveXY(self,x,y):  
        self.x +=x
        self.y +=y
        
  def moveToXY(self,x,y):  
        self.x =x
        self.y =y
        
  def reColor(self,c):
    self.color = c
    self.body.setFill(self.color)
    self.head.setFill(self.color) 
        
        

  def update(self):
        x = self.x-self.oldx
        y = self.y-self.oldy
        self.body.move(x, y)
        self.head.move( x, y)
        self.label.move( x, y)

        self.oldx = self.x;
        self.oldy = self.y;
        
  def move(self):  
        x = 1*self.size * math.cos(self.angle)
        y = 1*self.size * math.sin(self.angle)
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
          self.dx = (self.x - other.x)/4
          self.dy = (self.y - other.y)/4
 
        return c
  def collisionObs(self, obs): 
        c =  math.hypot(obs.getX() - self.x, obs.getY()  - self.y) < self.size + 12
        if c:
          self.dx = (self.x - obs.getX())/2
          self.dy = (self.y - obs.getY())/2
 
        return c
  def collisionWall(self):
        h = 20 +len(level)*40
 
        if self.x < 80:
          self.dx = 80- self.x
          return   True
        if self.y < 60:
          self.dy = 60- self.y
          return  True
        if self.x > 280:
          self.dx = 280 - self.x
          return  True
        if self.y > h:
          self.dy = h - self.y
          return   True
        return False
  def allign(self):
        self.moveXY(self.dx,self.dy)
        self.dx = 0;
        self.dy = 0;
  def getBlcok(self,vx,vy):
        i =  int((vx-60) / 40) 
        j =  int((vy-40) / 40)
        if  i < 0:
            return "none"
        if  j < 0:
            return "none"
        if  i > 5 :
            return "none"
        if j > 13:
            return "none"
        s = level[j][i]
        if s== "gray":
          s = "none" 
        return s;
  def sees(self):
        vx = self.x+2*self.size * math.cos(self.angle)
        vy = self.y+2*self.size * math.sin(self.angle)
        return self.getBlcok(vx, vy);

  def select(self, point):
        sel =  math.hypot(point.getX() - self.x, point.getY() - self.y) < self.size

        return sel
        
obstacles = []      
     
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
          if c != "gray" :
            obstacles.append(Point(xx+10, yy+10))
            obstacles.append(Point(xx+30, yy+10))
            obstacles.append(Point(xx+10, yy+30))
            obstacles.append(Point(xx+30, yy+30))
          box.setFill(c)
          box.draw(win)
    props = ai.make(10)
    #print(str(props))
    minions = []
    for i, p in enumerate(props):
      minions.append(Minion( i ,p, random.randint(56, 250), random.randint(500, 600),win)) 
     
    selectedParent = None
    animatioCounter = 0
    while(True):
      sees =[]
      for m in minions:
        sees.append(m.sees())

      #print(str(sees))
      actions = ai.see(sees)
      for m , act in zip(minions, actions):
        direction = 0
        if act == "left":
            direction = 1
        if act == "right":
            direction = -1
        if random.randint(0, 19) == 0:
            direction =  random.randint(-1, 1)
        m.turn(direction);
        if direction == 0:
          m.move()
      collision = True
      tries = 0;
      while collision and tries < 100: 
        collision = False
        tries += 1
        for m in minions: 
          for t in minions:
            if m is not t:
              c = m.collision(t)
              #print(c)
              if c:
                collision = True
          for o in obstacles: 
              c = m.collisionObs(o) 
              if c:
                collision = True
          c = m.collisionWall()
          if c:
                collision = True     
        for m in minions: 
          m.allign()

      for m in minions:
        m.update()
      time.sleep(.1)
      #print("----")
      if animatioCounter >= 10:
        animatioCounter = 0
        point  = win.getMouse()
        selected = None
        for i, m in enumerate(minions):
          if m.select(point):
            selected = i
        if selected is not None:
          if selectedParent is None:
            selectedParent = selected
            print("sel " + str(selectedParent))
          else:
            print("breeding " + str(selectedParent) +"  "+ str(selected))
            ai.breed(selectedParent, selected) 
            props = ai.make(10)
            selectedParent = None

            for m, p in zip(minions, props):
              m.moveToXY(random.randint(56, 250), random.randint(500, 600))
              m.allign()
              m.reColor(p)
      else:
        animatioCounter +=1
      win.flush() 
    win.close()
    

main()
