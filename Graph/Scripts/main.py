import matplotlib
matplotlib.use('TkAgg')
from skimage import io, color

from matplotlib import pyplot as plt
import string
import random

import Cluster
import Node

__author__ = 'Camilo'


def main(img_path, threshold):
    image_array = io.imread(img_path)
    lab = color.rgb2lab(image_array)

    pixels = graph_cut(image_array, lab, threshold)

    plt.subplot(121), plt.imshow(plt.imread(img_path)), plt.title('Original')
    plt.subplot(122), plt.imshow(pixels), plt.title('Using GraphCut')
    plt.show()


def graph_cut(pixels, lab, threshold):
    rows = range(len(pixels))
    cols = range(len(pixels[0]))

    graph = create_graph(rows, cols)

    init_graph(graph, rows, cols, pixels, lab)
    prim(graph)
    exit()

    clusters,paths = create_clusters(graph, threshold)

    color_clusters(clusters)

    #for node in paths:
    #    node.set_colors([0,255,0])

    build_image(pixels, graph, rows, cols)

    return pixels


def create_graph(rows, cols):
    print("Creating Graph...")
    return [[Node.Node(i, j, random.choice(string.ascii_letters)) for j in cols] for i in rows]


def init_graph(graph, rows, cols, pixels, lab):
    print("Calculating colors and linking neighbors for each node...")
    for i in rows:
        for j in cols:
            current_node = graph[i][j]

            current_node.set_values(pixels[i][j], lab[i][j])

            if i > 0:
                neighbor = graph[i - 1][j]
                current_node.neighbors.append(neighbor)

            if j > 0:
                neighbor = graph[i][j - 1]
                current_node.neighbors.append(neighbor)
                if i > 0:
                    neighbor = graph[i - 1][j - 1]
                    current_node.neighbors.append(neighbor)
                if i < (len(rows) - 1):
                    neighbor = graph[i + 1][j - 1]
                    current_node.neighbors.append(neighbor)

            if j < (len(cols) - 1):
                neighbor = graph[i][j + 1]
                current_node.neighbors.append(neighbor)
                if i > 0:
                    neighbor = graph[i - 1][j + 1]
                    current_node.neighbors.append(neighbor)
                if i < (len(rows) - 1):
                    neighbor = graph[i + 1][j + 1]
                    current_node.neighbors.append(neighbor)

            if i < (len(rows) - 1):
                neighbor = graph[i + 1][j]
                current_node.neighbors.append(neighbor)


def prim(graph):
    mst = [random.choice(random.choice(graph))]
    for row in graph:
        for node in row:
            for neighbor in node.neighbors:
                pass



def create_clusters(graph, threshold):
    print("Making cuts and creating clusters...")

    paths = []

    clusters = []
    for row in graph:
        for node in row:
            for neighbor in node.neighbors:
                delta_e = get_color_difference(neighbor.lab_values, node.lab_values)

                if delta_e > threshold:
                    node.cut(neighbor)
                    neighbor.cut(node)

            for cluster in clusters:
                if cluster.belongs_to_cluster(node, threshold):
                    cluster.nodes.append(node)
                    node.cluster()
                    break

            if not node.clustered:
                new_cluster = Cluster.Cluster()
                new_cluster.add_node(node)
                clusters.append(new_cluster)

    return clusters,paths

def contour(cluster):
    for cluster in clusters:
        cluster.color_edge()


def color_clusters(clusters):
    print("Recoloring clusters...")
    colors_used = []
    for cluster in clusters:
        cluster.colourify(colors_used)


def build_image(pixels, graph, rows, cols):
    print("Constructing Image...")
    for i in rows:
        for j in cols:
            current_node = graph[i][j]
            pixels[i][j] = current_node.rgb_values


# Color difference using the Cie76 Formula
def get_color_difference(lab1, lab2):
    return ((lab2[0] - lab1[0]) ** 2 + (lab2[1] - lab1[1]) ** 2 + (lab2[2] - lab1[2]) ** 2) ** 0.5


main("../Img/lol.jpg", 15)
