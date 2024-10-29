import pygame
import random

CELL_SIZE = 5
GRID_WIDTH = 100
GRID_HEIGHT = 100
SCREEN_WIDTH = GRID_WIDTH * CELL_SIZE
SCREEN_HEIGHT = GRID_HEIGHT * CELL_SIZE + 50


class Board:
    def __init__(self, r, c):
        self.r = r
        self.c = c
        self.board_original = [[0 if random.random() < .9 else 1 for _ in range(c)] for _ in range(r)]
        self.board = self.board_original

        self.gens = 0
        self.running = False

    def toggle_running(self):
        self.running = not self.running
    
    def reset(self):
        self.board = self.board_original
        self.running = False
    
    def get_num_neighbors(self, row, col):
        row_p1 = (row+1) % self.r
        col_p1 = (col+1) % self.c
        row_m1 = (row-1) % self.r
        col_m1 = (col-1) % self.c
        board = self.board

        return sum([
            board[row_m1][col_m1],
            board[row_m1][col],
            board[row_m1][col_p1],
            board[row][col_p1],
            board[row_p1][col_p1],
            board[row_p1][col],
            board[row_p1][col_m1],
            board[row][col_m1],
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
        next_board = [[0 for _ in range(self.c)] for _ in range(self.r)]
        for row in range(self.r):
            for col in range(self.c):
                next_board[row][col] = Board.next_state(
                    self.board[row][col], 
                    self.get_num_neighbors(row, col)
                )
        self.board = next_board
        self.gens += 1

if __name__ == "__main__":
    pygame.init()
    screen = pygame.display.set_mode((SCREEN_WIDTH, SCREEN_HEIGHT))
    pygame.display.set_caption("Game of Life")

    WHITE = (255, 255, 255)
    BLACK = (0, 0, 0)
    BLUE = (0, 0, 255)
    GREEN = (0, 255, 0)
    GRAY = (122, 122, 122)

    board = Board(GRID_HEIGHT, GRID_WIDTH)

    font = pygame.font.Font(None, 36)
    button_text = font.render("Start", True, WHITE)
    button_rect = pygame.Rect(10, SCREEN_HEIGHT - 40, 80, 30)

    clock = pygame.time.Clock()
    running = True
    while running:
        screen.fill(WHITE)
        button_text = font.render("Stop" if board.running else "Start", True, WHITE)
        pygame.draw.rect(screen, GRAY, (0, GRID_HEIGHT * CELL_SIZE, SCREEN_WIDTH, SCREEN_HEIGHT - GRID_HEIGHT * CELL_SIZE))

        # Draw grid
        for row in range(board.r):
            for col in range(board.c):
                color = BLACK if board.board[row][col] == 1 else WHITE
                pygame.draw.rect(screen, color, (col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE))

        # Draw button
        pygame.draw.rect(screen, BLUE, button_rect)
        screen.blit(button_text, (20, SCREEN_HEIGHT - 35))

        # Draw generation text
        gen_text = font.render(f"Generation: {board.gens}", True, GREEN)
        screen.blit(gen_text, (SCREEN_WIDTH - 200, SCREEN_HEIGHT - 40))

        # Update cells if running
        if board.running:
            board.update()

        # Event handling
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False
            elif event.type == pygame.MOUSEBUTTONDOWN:
                if button_rect.collidepoint(event.pos):
                    board.toggle_running()
                    button_text = font.render("Stop" if board.running else "Start", True, WHITE)

        pygame.display.flip()
        clock.tick(10)  # Update 10 frames per second

    pygame.quit()


