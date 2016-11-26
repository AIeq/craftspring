class Genome:  
  def __init__(self, parents = None): #[Genome, Genome]
        self.genes = None
        
  def getMove(self, input): # color
    return "forward";
    
  def getPhysicalProperties(self):
    return {"color": "black", "speed":1, "health": 1, "damage": 1, "lifetime": 20};
    
  def __repr__(self):
    return str(self.genes)
