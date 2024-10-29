from matplotlib.widgets import Button
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation
import random

class Board:
    def __init__(self, r, c):
        self.board = [[0 if random.random() < .9 else 1 for _ in range(c)] for _ in range(r)]
        self.gens = 0
        self.running = True

        plt.ion()  # Interactive mode on
        fig, ax = plt.subplots()
        self.im = ax.imshow(self.board, cmap='gray', vmin=0, vmax=1)
        self.text_display = ax.text(0.5, 1.1, "Generation 0", ha="center", va="center", transform=ax.transAxes, color="blue", fontsize=14)
        
        button_ax = plt.axes([0, 0, 0.2, 0.075])  # [left, bottom, width, height]
        button = Button(button_ax, 'Start')
        button.on_clicked(self.play_pause)

        global anim
        anim = FuncAnimation(fig, self.animate, interval=1000, blit=False, save_count=100, cache_frame_data=False)
        self.anim = anim
        plt.show()

    def play_pause(self):
        self.running = not self.running

    def get_rc(self):
        return (len(self.board), len(self.board[0]))
    
    def get_num_neighbors(board, row, col):
        row_1 = row+1 if row < len(board)-1 else 0
        col_1 = col+1 if col < len(board[0])-1 else 0

        return sum([
            board[row-1][col-1],
            board[row-1][col],
            board[row-1][col_1],
            board[row][col_1],
            board[row_1][col_1],
            board[row_1][col],
            board[row_1][col-1],
            board[row][col-1],
        ])

    def next_state(state, num_neighbors):
        if state == 1:
            if num_neighbors < 2:
                return 0
            if num_neighbors > 3:
                return 0
            return 1
        return num_neighbors == 3
    
    def update(self):
        r, c = self.get_rc()
        next_board = [[0 for _ in range(c)] for _ in range(r)]
        for row in range(r):
            for col in range(c):
                next_board[row][col] = Board.next_state(
                    self.board[row][col], 
                    Board.get_num_neighbors(self.board, row, col)
                )
        self.board = next_board
        self.gens += 1

    def display(self):
        self.im.set_data(self.board)
        self.text_display.set_text(f"Generation {self.gens}")
        plt.draw()
        plt.pause(0.1)
    
    def animate(self):
        if self.running:
            self.update()
            self.display()

if __name__ == "__main__":
    board = Board(100, 100)


