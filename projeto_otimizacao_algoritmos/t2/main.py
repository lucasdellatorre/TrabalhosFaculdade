# from dataclasses import replace
# import numpy as np

# negative_inf = float("-inf")

# def load_maze(filename):
#     """Load maze from file."""
#     with open(filename, 'r') as f:
#         lines = f.readlines()
#         size = int(lines[0].replace("\n", ""))
#         

#         for i, line in enumerate(lines[1:]):
#             for j, char in enumerate(line.replace("\n", "").replace(" ", "")):
#                 maze[i, j] = int(char)

#     return maze, size

# def load_maze(filename):
#     with open('') as f:
#         for line in f:
#             inner_list = [elt.strip() for elt in line.split(',')]
#             # in alternative, if you need to use the file content as numbers
#             # inner_list = [int(elt.strip()) for elt in line.split(',')]
#             list_of_lists.append(inner_list)

# def findPath(val):
#     if val == 0: return 0
#     if val < 0:  return negative_inf
#     print('hello')

# matrix = load_maze('teste1.txt')

# print(matrix)



