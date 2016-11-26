from Genome import Genome


class ListGenome(Genome):  
  def __init__(self, parents = None): #[Genome, Genome]
        self.genes = ["ttt"]
        
  def getMove(self, input): # color
    return "forward";
 
  def findColor(self): # color
    return "blue";
    
  def getPhysicalProperties(self):
    return {"color": self.findColor(), "speed":1, "health": 1, "damage": 1, "lifetime": 20};
     
  def mutate(self, propability = 10, hint = None):
    "not implemented"
    
  def __repr__(self):
    return str(self.genes)