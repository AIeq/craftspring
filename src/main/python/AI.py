    
# Defining a class
class Creature:
  def __init__(self, x):
        self.x = x
  def move(self, input):
    if(input == "b"):
      return "f";
    else:
      return "r";
      
class Ai:
  def __init__(self): 
    self.creatures = []
    self.lastOutput = ""
    
  def output(self, s):
    self.lastOutput = s
    print(s)
    
  def getLastOutput(self):
    return self.lastOutput
    
  def run(self):
    running = True;  
    self.output("AI started, give commands")
    while(running):
      try:
        line = input().split(' ', 1)
        command = line[0]
        try:
          args = line[1].split(' ')
        except Exception  as e:
          args = "none"

        if command == "exit":
          running = False;
        elif command == "make":
          self.output("ok") 
          self.creatures = []
          for count in range(int(args[0])):
            x = Creature(3) 
            self.creatures.append(x)
        elif command == "move":
          result = ""
          for a, c in zip(args, self.creatures): 
            result = result + " " + c.move(a)
          
          self.output("moving" + result)
        elif command == "print":
          self.output("creatures " + str(self.creatures))
        else :
          self.output("unknown command " + command)
      except Exception  as e:
        self.output (" Exception:  " + str( e))

if __name__ == "__main__":
  Ai().run()

  
