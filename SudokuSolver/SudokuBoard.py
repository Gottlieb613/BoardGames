import tkinter as tk
import pygame

pygame.init()
font = pygame.font.SysFont('Arial', 25)
screen_h = 500
screen_w = 500
win = pygame.display.set_mode((screen_w, screen_h)) #width, hight
win.fill((0,0,0))
pygame.display.set_caption("Charlie's Sudoku")

box_side = 10

black = (255,255,255)

class SudokuBoard:    
    class Cluster:
        def __init__(self):
            self.contents = []
        
        def append(self, num):
            if self.check_in(num):
                return False
            else:
                self.contents.append(num)
                return True

        def check_in(self, num):
            return num in self.contents
        
        def __contains__(self, num):
            return num in self.contents
    
    class Cell:
        def __init__(self, x, y, val = None):
            self.EMPTY_CELL_VAL = "."
            if not val:
                self.val = self.EMPTY_CELL_VAL
            else:
                self.val = val
            self.original = True
                #keeps track of if this Cell is given originally by the board (T)
                # or changed by the user (F)
            self.x = x #row
            self.y = y #column

            #calculating which cluster it is a part of
            clus_x = x // 3
            clus_y = y // 3
            self.cluster_ind = (3 * clus_x) + clus_y 

        def get_val(self):
            return self.val
        
        def update(self, new_val):
            if 1 <= new_val <= 9 and isinstance(new_val, int):
                self.val = new_val
                self.original = False
    
        def delete(self):
            if self.original:       #Cannot delete if it's original
                return False
            self.val = self.EMPTY_CELL_VAL
            self.original = True    #Return to original after deletion
            return True

        def get_cluster_ind(self):
            return self.cluster_ind
            
        def is_empty(self):
            return self.val == self.EMPTY_CELL_VAL

        def __repr__(self):
            return str(self.val)
    
    #----------------------------------------------------------------------------------
    def __init__(self, board = None):
        if board:
            self.board = board
        else:
            self.board = [[SudokuBoard.Cell(x, y) for x in range(9)] for y in range(9)]
        self.clusters = [SudokuBoard.Cluster() for _ in range(9)]

    def update_cell(self, x, y, new_val):
        if self.is_valid_move(x, y, new_val):
            self.clusters[self.board[x][y].cluster_ind].append(new_val)
            self.board[x][y].update(new_val)
            return True
        return False

    def delete_cell(self, x, y):
        return self.board[x][y].delete()
        

    #def bt_solver(self): #bt = backtrack

    def is_valid_move(self, x, y, new_val):
        if not isinstance(new_val, int) or not (1 <= new_val <= 9) or not (0 <= x <= 8) or not (0 <= y <= 8):
            return False #if the input itself is invalid (not num or not in range)
        
        if self.board[x][y].original and not self.board[x][y].val == self.board[0][0].EMPTY_CELL_VAL:
            return False #cannot override original

        row = [cell.val for cell in self.board[x]]
        row[y] = None #So the cell in question is not counted
        col = [self.board[i][y].val for i in range(9)]
        col[x] = None #So the cell in question is not counted
        clus = self.clusters[self.board[x][y].cluster_ind]

        return (new_val not in row) and (new_val not in col) and (new_val not in clus)
    
    def cell_empty(self, x, y):
        return self.board[x][y].is_empty()
    
    def print_board(self):
        for i in range(9):
            if i % 3 == 0 and i > 0:
                for col in range(17):
                    if (col+1) % 6 == 0:
                        print("+", end ="")
                    else:
                        print("-", end="")
                print()
            
            for j in range(9):
                curr_val = str(self.board[i][j].get_val())
                if (j+1) % 3 == 0 and j < 8:
                    print(curr_val, "|", sep="",end="")
                else:
                    print(curr_val, end=" ")
            print()

'''
    def draw_board(self):
        for i in range(9):
            for j in range(9):
                curr_cell = str(self.board[i][j].get_val)
                cell = pygame.Rect(i*box_side, j*box_side, box_side, box_side)
                pygame.draw.rect(win, black, cell, border_radius=1)
                win.blit(font.render(curr_cell, True, black), box_side, box_side)
'''


def playing(board):
    while True:
        x = int(input("x:"))
        y = int(input("y:"))
        val = int(input("val:"))

        if val == -1:
            if board.delete_cell(x, y):
                print("Deleted")
                board.print_board()
            else:
                print("That is an original cell. Cannot delete")

        else:
            if board.update_cell(x, y, val):
                print("Good choice!")
                board.print_board()
            else:
                print("cannot go there, try again")

def main():
    
    pygame.init()
    screen_h = 500
    screen_w = 500
    win = pygame.display.set_mode((screen_w, screen_h)) #width, hight
    pygame.display.set_caption("Charlie's Sudoku")

    s = SudokuBoard()


    s.print_board()
    #s.draw_board()
    print()

    playing(s)
    #pygame.display.update()
        

main()