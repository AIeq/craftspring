 
from Genome import DummyGenome
from ListGenome import ListGenome

genomeType = "list"
def genomeFactory(parents):
  if genomeType is "dummy":
    return DummyGenome(parents);
  elif genomeType is "map":
    return MapGenome(parents);
  elif genomeType is "list":
    return ListGenome(parents);

# Defining a class
class Creature:
  def __init__(self, genome):
        self.genome = genome
        
  def __repr__(self):
    return "Creature" + str(self.genome) 
    
  def move(self, input):     
    return self.genome.getMove(input)
    
  def properties(self):
    return str(self.genome.getPhysicalProperties())
      
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
    parents = None;
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
          self.creatures = [] 
          result = ""
          for count in range(int(args[0])):
            g = genomeFactory(parents)
            
            c = Creature(g) 
            self.creatures.append(c)  
            result = result + " " + c.properties()
          
          self.output("properties" + result) 
        elif   command == "see":
          result = ""
          for a, c in zip(args, self.creatures): 
            result = result + " " + c.move(a)
          
          self.output("move" + result)
        elif command == "breed":
          parents = [self.creatures[int(args[0])], self.creatures[int(args[1])]];
          
        elif command == "print":
          self.output(str(len(self.creatures))  + " creatures " + str(self.creatures))
        else :
          self.output("unknown command " + command)
      except Exception  as e:
        self.output (" Exception:  " + str( e))

if __name__ == "__main__":
  Ai().run()
  print("bye")

  
