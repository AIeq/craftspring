class Genome: 
  def getMove(self, input): # color
    raise NotImplementedError( "Should have implemented this" )
    
  def getPhysicalProperties(self):
    raise NotImplementedError( "Should have implemented this" )
     
  def mutate(self, propability = 10, hint = None):
    raise NotImplementedError( "Should have implemented this" )
    
  def __repr__(self):
    raise NotImplementedError( "Should have implemented this" )
    
    
    
    
class DummyGenome(Genome):  
  def __init__(self, parents = None): #[Genome, Genome]
        self.genes = None
        
  def getMove(self, input): # color
    return "forward";
    
  def getPhysicalProperties(self):
    return {"color": "black", "speed":1, "health": 1, "damage": 1, "lifetime": 20};
     
  def mutate(self, propability = 10, hint = None):
    "not implemented"
    
  def __repr__(self):
    return str(self.genes)