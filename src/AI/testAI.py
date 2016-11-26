import sys
import unittest
from  unittest import mock
from AI import Ai

class TestMoveCommands(unittest.TestCase):
  @mock.patch('builtins.input', side_effect=[
  'create 2',
  'move r b',
  'exit'])
  def testMove2(self, input):
      ai = Ai()
      ai.run()
      self.assertEqual(ai.getLastOutput(), "moving r f")
      
  @mock.patch('builtins.input', side_effect=[
  'create 5',
  'move r b r b r',
  'exit'])
  def testMove5(self, input):
      ai = Ai()
      ai.run()
      self.assertEqual(ai.getLastOutput(), "moving r f r f r")

if __name__ == '__main__':
    unittest.main()