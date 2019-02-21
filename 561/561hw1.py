
#########################################
# 输出整个矩阵
def write_board(board):
    N = len(board)
    f = open("output.txt", 'w')
    f.write(str(N) + '\n')
    for i in range(0, N):
        for j in range(0, N):
            f.write(str(board[i][j]))
        f.write('\n')
    f.close()


def generate_matrix(width, num_block):
    import random
    m = [[0 for _ in range(width)] for _ in range(width)]
    now_block = 0
    while now_block < num_block:
        i = random.randint(0, width-1)
        j = random.randint(0, width-1)
        if m[i][j] == 0:
            m[i][j] = 3
            now_block += 1
    return m


def change_matrix(matrix, x, y, p):
    matrix[x][y] = p
    return matrix


def show_matrix(matrix):
    for m in matrix:
        for n in m:
            print("%2.0f" % n, end=' ')
        print()

############################################
import copy

class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __str__(self):
        return self.x, self.y


class Node:
    def __init__(self, matrix, t):
        self.matrix = matrix
        self.t = t
        self.score = 0
        self.children = []
        self.parent = None
        self.cur_sel = None
        self.point = None
        self.select_point = None
        self.a = -999999
        self.b = 999999
        self.level = 0
        self.is_finish = False

def create_sub_node(parent, is_bottom= False):
    pm = parent.matrix
    cur_score = parent.score
    for i in range(len(pm)):
        for j in range(len(pm[0])):
            if pm[i][j] != 0:
                continue
            s, child_matrix = count_fill(pm, i, j, parent.t)

            if parent.t == 1:
                s += cur_score
            else:
                s = cur_score - s

            cn = Node(child_matrix, parent.t % 2 + 1)
            cn.point = Point(i, j)
            cn.parent = parent
            cn.score = s
            cn.level = parent.level + 1

            cn.a, cn.b = parent.a, parent.b

            parent.children.append(cn)
            parent.score = -99999 if parent.t == 1 else 99999
    return parent.children


# Return whether we should stop
def minimax(np, nc):
    if np.t == 1:
        if np.score < nc.score:
            np.score = nc.score
            np.select_point = copy.copy(nc.point)
        if np.score > np.b:
            return True
        else:
            np.a = max(np.a, np.score)
    else:
        if np.score > nc.score:
            np.score = nc.score
            np.select_point = copy.copy(nc.point)
        if np.score < np.a:
            return True
        else:
            np.b = min(np.b, np.score)

    return False


def generate_branch(stack, search_level):
    # generate branch
    top = stack[-1]
    while top.level < search_level and not top.is_finish:
        top.is_finish = True
        children_list = create_sub_node(top)
        if top.t == 2:
            children_list.sort(key=lambda c: c.score)
        else:
            children_list.sort(key=lambda c: c.score, reverse=True)
        if children_list:
            stack.append(children_list[0])
        top = stack[-1]

"""
in matrix
0: empty
1: my laser emitters
2: opponent's laser emitters
3: a block
-1: covered by mine
-2: covered by opponent's
-3: covered by both of users
"""
def game(matrix, search_level=4):
    root = Node(matrix, 1)
    root.score = 0
    stack = [root]
    cc = 0
    lc = 0

    while stack:

        if stack[-1].children:
            child = stack[-1].children[0]
            child.a = stack[-1].a
            child.b = stack[-1].b
            stack.append(child)

        if not stack[-1].is_finish:
            generate_branch(stack, search_level)

        if stack[-1].parent:
            stack[-1].parent.children.pop(0)
        cur_node = stack.pop()



        if cur_node.point and cur_node.point.x == 1 and cur_node.point.y == 0 and cur_node.level == 1:
            # print(1111)
            pass

        if cur_node.level == 1:
            lc += 1
            print(cur_node.point.x,cur_node.point.y,cur_node.level,cur_node.score)
        # print(cur_node.level)
        cc += 1
        if cur_node.parent:
            if minimax(cur_node.parent, cur_node):
                cur_node.parent.children = []
                pass

    print(cc)
    return root.select_point.x, root.select_point.y

# decide the number of layer to explore
def decide_layer(matrix):
    width = len(matrix)
    num_zero = 0
    for i in range(len(matrix)):
        for j in range(len(matrix[0])):
            if matrix[i][j] == 0:
                num_zero += 1
    layer = 1
    if width > 21:
        layer = 1
    elif width > 15:
        layer = 2
    elif width > 10:
        layer = 3
    else:
        layer = 14 - width

    return layer


def count_fill(matrix1, x, y, t):
    width = len(matrix1)
    matrix = copy.deepcopy(matrix1)
    # if matrix[x][y] != 0:
    #     return 0
    # else:
    #     matrix[x][y] = t
    matrix[x][y] = t
    # the initial point score
    score = 1

    for i in [-1, 0, 1]:
        for j in [-1, 0, 1]:
            k = 1
            is_block = False
            while k < 4 and not is_block:
                if i == 0 and j == 0:
                    break

                cx = x + k * i
                cy = y + k * j

                # whether it is beyond bound
                if cx < 0 or cx >= width or cy < 0 or cy >= width:
                    is_block = True
                    continue

                # whether it is blocked or not
                if matrix[cx][cy] == 3:
                    is_block = True
                    continue

                # whether it is covered by both users
                if matrix[cx][cy] == -3:
                    k += 1
                    continue

                # determine whether it is my movement or opponent's.
                if matrix[cx][cy] == 0:
                    matrix[cx][cy] = -t

                # fill the color of both users
                if t == 1:
                    if matrix[cx][cy] == -2:
                        matrix[cx][cy] = -3
                else:
                    if matrix[cx][cy] == -1:
                        matrix[cx][cy] = -3
                # filled with color
                score += 1
                k += 1
    return score, matrix





def out_put(x,y):
    with open("output.txt","w") as f:
        f.write(str(x) + ' ' + str(y))





def read_file(file_name):
    with open(file_name, "r") as f:
        ls = f.readlines()
    w = int(ls[0])
    matrix = [[0 for _ in range(w)] for _ in range(w)]
    laser_list = []
    for i, l in enumerate(ls[1:]):
        for j, c in enumerate(l[:-1]):
            matrix[i][j] = int(c)
            if int(c) == 1 or int(c) == 2:
                laser_list.append(Point(i, j))

    o_matrix = copy.deepcopy(matrix)
    for p in laser_list:
        s, matrix = count_fill(matrix, p.x, p.y, matrix[p.x][p.y])


    return w, matrix,o_matrix


"""
6
000003
000000
000030
030000
003000
000000
"""

if __name__ == '__main__':
    import datetime


    starttime = datetime.datetime.now()
    w = 9
    matrix = generate_matrix(w,int(w*w*0.2))
    # write_board(m)
    # w, matrix,om = read_file('input.txt')
    print(decide_layer(matrix))
    show_matrix(matrix)
    print()
    # show_matrix(om)
    x,y = game(matrix,decide_layer(matrix))
    # om = change_matrix(om,x,y,1)
    # write_board(om)
    # print(x,y)
    # show_matrix(om)
    # s,m = count_fill(w,matrix,x,y,1)
    # show_matrix(m)
    out_put(x,y)
    # long running
    # do something other
    endtime = datetime.datetime.now()
    print("time:")
    print((endtime - starttime).seconds)
