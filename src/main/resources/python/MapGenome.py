from Genome import Genome
from crossing import Minion
 
class MapGenome(Genome):  
  def __init__(self, parents = None): #[Genome, Genome] 
    if parents is not None:
      self.minion = Minion(parents[0].minion, parents[1].minion)
    else :
      self.minion = Minion()
        
  def getMove(self, input): # color
    if input == "none" :
      return "forward"
    return self.minion["triggers"][input]
   
    
  def getPhysicalProperties(self):
    #return {"color": self.findColor(), "speed":1, "health": 1, "damage": 1, "lifetime": 20};
    return self.minion["color"]
    
  def mutate(self, propability = 10, hint = None):
    self.minion.mutate()
    
  def __repr__(self):
    return str(self.minion)
     