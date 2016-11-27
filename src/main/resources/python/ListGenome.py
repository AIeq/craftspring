from Genome import Genome
import random

colors = ["black", "white", "red", "green", "blue"]
actions = ["left", "right", "attack" ]
possibleGenes = colors + actions

class ListGenome(Genome):  
  def __init__(self, parents = None): #[Genome, Genome] 
        if parents is None:   
            self.genes = []
            for _ in range(6):
              self.genes.append( self.genes.append(random.choice(possibleGenes)))
        else:
          self.genes = []
          for a,b in zip(parents[0], parents[1]): 
            self.genes.append( random.choice([a,b]) )
        
  def getMove(self, input): # color
    active = False
    for gene in self.genes:
      if gene == input:
        active = True
      if active and gene in actions:
        return gene
    return "forward";
 
  def findColor(self): # color
    for gene in self.genes:
      if gene in colors:
        return gene;
    return "black";
    
  def getPhysicalProperties(self):
    #return {"color": self.findColor(), "speed":1, "health": 1, "damage": 1, "lifetime": 20};
    return self.findColor()
    
  def mutate(self, propability = 10, hint = None):
    for i, gene in enumerate(self.genes):
      if random.randint(0, 99) < propability:
        self.genes[i] = random.choice(possibleGenes)  
    
  def __repr__(self):
    return str(self.genes)