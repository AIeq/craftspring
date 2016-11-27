import random

ACTIONS = ["forward", "left", "right", "jump", "attack"]
PHYSICALS = ["color", "speed", "health", "damage", "lifetime"]
COLORS = ["blue", "red", "white", "green"]


class Minion(dict):
    def __init__(self, *parents):
        super().__init__()
        if not parents:
            self["triggers"] = {color: "forward" for color in COLORS}
            self["color"] = random.choice(COLORS)
            self["speed"] = random.choice([1, 2])
            self["health"] = random.choice([1, 2, 3])
            self["damage"] = random.choice([1, 2, 3])
            self["lifetime"] = random.choice(list(range(15, 30)))
        else:
            self.update(cross(parents[0], parents[1]))

        self.mutate_counter = 0

    def mutate(self):
        trigger = random.choice(list(self["triggers"]))
        self["triggers"][trigger] = random.choice(ACTIONS)
        for physical in PHYSICALS:
            if random.choice([True]) and isinstance(self[physical], (int, float)):
                multipliers = [(self.mutate_counter * 0.03 + 1) * mult for mult in [
                               0.2, 0.5, 0.75, 0.8, 0.9, 1.1, 1.2, 1.3, 5, 7]]
                new_value = self[physical] * random.choice(multipliers)
                new_value = max(0, new_value)
                self[physical] = new_value
        self.mutate_counter += 1
        return self

    def __str__(self):
        return "Minion(%s)" % super().__str__()


def cross(m1, m2):
    new_minion = Minion()
    for color in COLORS:
        m1_acton, m2_action = m1["triggers"][color], m2["triggers"][color]
        new_minion["triggers"][color] = random.choice([m1_acton, m2_action])
    for physical in PHYSICALS:
        m1_phys, m2_phys = m1[physical], m2[physical]
        new_minion[physical] = random.choice([m1_phys, m2_phys])
    return new_minion


if __name__ == "__main__":
    m1 = Minion()
    m2 = Minion()
    m5 = Minion(m1, m2).mutate().mutate().mutate()
    print(m5)
