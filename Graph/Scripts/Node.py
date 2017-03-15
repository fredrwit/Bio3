__author__ = 'Camilo'


class Node(object):
    def __init__(self, i, j, letter):
        self.id = str(i) + letter + str(j)
        self.rgb_values = [0, 0, 0]
        self.lab_values = [0, 0, 0]
        self.clustered = False
        self.cluster_id = None
        self.neighbors = []
        self.center = None

    def set_values(self, rgb, lab):
        self.rgb_values = rgb
        self.lab_values = lab

    def add_neighbor(self, node):
        self.neighbors.append(node)

    def cluster(self,cluster_id):
        self.clustered = True
        self.cluster_id = cluster_id

    def set_center(self, center):
        self.center = center

    def set_colors(self, colors):
        self.rgb_values = colors

    def cut(self, neighbor):
        self.neighbors = [x for x in self.neighbors if neighbor.__eq__(x)]

    def __eq__(self, other):
        return self.id != other.id

    def get_cluster(self):
        return self.cluster_id

