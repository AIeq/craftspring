 
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
  def getGenome(self):
    return  self.genome
      
class Ai:
  def __init__(self): 
    self.creatures = []
    self.lastOutput = ""
    self.parents = None;
    
  def output(self, s):
    self.lastOutput = s
    print(s)
    
  def getLastOutput(self):
    return self.lastOutput
  
  def make(self, n):
    self.creatures = [] 
    result = []
    for i in range(n):
      if self.parents is not None and i == 0:
        g = self.parents[0]
      elif self.parents is not None and i == 1:
        g = self.parents[1]
      else:
        g = genomeFactory(self.parents)
        g.mutate()
      c = Creature(g) 
      self.creatures.append(c)  
      result.append( c.properties())
    self.parents = None
    return result 
  def see(self, args):
    result = ""
    for a, c in zip(args, self.creatures): 
      result = result + " " + c.move(a)
    
    return result
  def breed(self, i,j):
    self.parents = [self.creatures[i].getGenome(), self.creatures[j].getGenome()]
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
          self.output("properties [" + ", ".join (self.make(int(args[0]))) + "]")
        elif   command == "see":
          self.output("move" + self.see(args))
        elif command == "breed":
          self.breed( int(args[0])  ,  int(args[1]) );
          
        elif command == "print":
          self.output(str(len(self.creatures))  + " creatures " + str(self.creatures))
        else :
          self.output("unknown command " + command)
      except Exception  as e:
        self.output (" Exception:  " + str( e))

if __name__ == "__main__":
  Ai().run()
  print("bye")

  
