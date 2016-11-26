import sys
import unittest
from  unittest import mock
from AI import Ai

class TestMoveCommands(unittest.TestCase):
  @mock.patch('builtins.input', side_effect=[
  'make 2',
  'see left left',
  'exit'])
  def testMove2(self, input):
      ai = Ai()
      ai.run()
      self.assertEqual(ai.getLastOutput(), "move forward forward")
      
  @mock.patch('builtins.input', side_effect=[
  'make 5',
  'see r b r b r',
  'exit'])
  def testMove5(self, input):
      ai = Ai()
      ai.run()
      self.assertEqual(ai.getLastOutput(), "move forward forward forward forward forward")

if __name__ == '__main__':
    unittest.main()