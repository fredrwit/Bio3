import random
import numpy as np

__author__ = "Camilo"


def get_color_difference(lab1, lab2):
    return ((lab2[0] - lab1[0]) ** 2 + (lab2[1] - lab1[1]) ** 2 + (lab2[2] - lab1[2]) ** 2) ** 0.5


def unique_colors(color, colors_used):
    for used in colors_used:
        if color == used:
            return True
    return False


class Cluster(object):
    def __init__(self):
        self.nodes = []
        self.color = [0, 0, 0]
        self.lab = [0, 0, 0]

    def add_node(self, node):
        self.nodes.append(node)
        self.color += node.rgb_values
        #self.color /= len(self.nodes)
        np.true_divide(self.color, 2, out=self.color, casting='unsafe')

        self.lab += node.lab_values
        self.lab /= len(self.nodes)

    def belongs_to_cluster(self, node, threshold):
        delta_e = get_color_difference(self.lab, node.lab_values)
        if delta_e > threshold:
            return False
        return True

    def colourify(self, colors_used):
        r = lambda: random.randint(0, 255)
        color = [r(), r(), r()]

        while unique_colors(color, colors_used):
            color = [r(), r(), r()]

        colors_used.append(color)

        self.color = color

        for node in self.nodes:
            node.set_colors(self.color)
