
import random
#0 = blue (top), 1 = red (bottom)

class Piece:
    def __init__(self, player, type, y, x):
        self.player = player #0=blue, 1=red
        self.type = type     #0=student, 1=master
        self.location = (y, x)
    
    def __str__(self):
        if self.player: #red
            if self.type: #master
                return 'R'
            #student
            return 'r'
        else: #blue
            if self.type: #master
                return 'B'
            #student
            return 'b'


class Card:
    def __init__(self, name, color, move_tiles, list_mode=False): 
        self.name = name
        self.color = color # -1=gray, 0=blue, 1=red
        self.player = 0
        if not list_mode: #moves is a 2d list, where each 1 is a place they can go to, relative to the position of the piece. Initialized as pointing 'down' (0's direction)
            self.moves = []
            for y in range(len(move_tiles)):
                for x in range(len(move_tiles[0])):
                    if move_tiles[y][x]:
                        self.moves.append((y - 2, x - 2)) #the -2 is to set (2, 2) as the midpoint (where the piece actually is)
        #moves is a list of moves of type (x, y), relative to the position of the piece. Initialized as pointing 'down' (0's direction)
        else:
            self.moves = move_tiles

    def flip(self):
        self.moves = [(-y, -x) for (y, x) in self.moves]
        self.change_player()
    
    def change_player(self):
        self.player = 1 - self.player
    
    def update_location(self, new_y, new_x):
        self.location = (new_y, new_x)


class Board:
    def __init__(self):
        self.board = [
            [Piece(0, 0, 0, 0), Piece(0, 0, 1, 0), Piece(0, 1, 2, 0), Piece(0, 0, 3, 0), Piece(0, 0, 4, 0)],
            [None, None, None, None, None],
            [None, None, None, None, None],
            [None, None, None, None, None],
            [Piece(1, 0, 0, 4), Piece(1, 0, 1, 4), Piece(1, 1, 2, 4), Piece(1, 0, 3, 4), Piece(1, 0, 4, 4)]
        ]

        self.cards = random.sample(Board.get_default_cards(), 5)

        self.players = [[self.cards[0], self.cards[1]], [self.cards[2], self.cards[3]]]
        self.players[1][0].flip()
        self.players[1][1].flip()
        self.next_card = (self.cards[4], 0) #(next_card, next_player)
    
    def print_board(self):
        for row in self.board:
            for tile in row:
                if tile:
                    print(tile, end='')
                else:
                    print('◻', end='')
            print()
    
    def get_player(player):
        if player:
            return "Red"
        return "Blue"
    
    def print_status(self):
        cur_player = self.next_card[1]
        self.print_board()
        print(f"Blue's moves: {self.players[0][0].name} (0), {self.players[0][1].name} (1)")
        print(f"Red's moves: {self.players[1][0].name} (0), {self.players[1][1].name} (1)")
        print(f"{Board.get_player(cur_player)} is up! Next card to receive: {self.next_card[0].name}")
    
    def add_card(self, card):
        self.cards.add(card)

    def used_card(self, player, used_card):
        self.players[player][self.players.index(used_card)] = self.next_card
        used_card.flip()
        self.next_card = (used_card, 1 - player)

    def card_move(self, card, move):
        return move in card
    
    def move_item(self, piece, location):
        pass

    def legal_spot(y, x):
        return (0 <= y <= 4) and (0 <= x <= 4)
    
    def get_tile(self, y, x):
        return self.board[y][x]
    
    def piece_can_move(self, piece, landing_y, landing_x):
        if Board.legal_spot(landing_y, landing_x):
            landing_piece = self.get_tile(landing_x, landing_y)
            if landing_piece is None or landing_piece.player != piece.player:
                return True
        return False
    
    # def move_piece(self, piece, move):
    #     landing_y = move[0] + piece.location[0]
    #     landing_x = move[1] + piece.location[1]
        
    #     if self.piece_can_move(piece, landing_y, landing_x):
    #         landing_piece = self.get_tile(landing_y, landing_x)
    #         if landing_piece == None or landing_piece.player != piece.player:
    #             self.board[landing_y][landing_x] = piece
    #             piece.location = (landing_x, landing_y)
    #             return 1, landing_piece
    
    #     return 0, None


    def get_default_cards():
        defaults = {
            Card('Boar', -1, [
                [0, 0, 0, 0, 0],
                [0, 0, 0, 0, 0],
                [0, 1, 0, 1, 0],
                [0, 0, 1, 0, 0],
                [0, 0, 0, 0, 0]]),
            Card('Frog', 0, [
                [0, 0, 0, 0, 0],
                [0, 1, 0, 0, 0],
                [0, 0, 0, 0, 1],
                [0, 0, 0, 1, 0],
                [0, 0, 0, 0, 0]]),
            Card('Tiger', -1, [
                [0, 0, 0, 0, 0],
                [0, 0, 1, 0, 0],
                [0, 0, 0, 0, 0],
                [0, 0, 0, 0, 0],
                [0, 0, 1, 0, 0]]),
            Card('Crane', -1, [
                [0, 0, 0, 0, 0],
                [0, 1, 0, 1, 0],
                [0, 0, 0, 0, 0],
                [0, 0, 1, 0, 0],
                [0, 0, 0, 0, 0]]),
            Card('Goose', 0, [
                [0, 0, 0, 0, 0],
                [0, 1, 0, 0, 0],
                [0, 1, 0, 1, 0],
                [0, 0, 0, 1, 0],
                [0, 0, 0, 0, 0]]),
            Card('Rabbit', 1, [
                [0, 0, 0, 0, 0],
                [0, 0, 0, 1, 0],
                [1, 0, 0, 0, 0],
                [0, 1, 0, 0, 0],
                [0, 0, 0, 0, 0]]) #need to add more
        }
        return defaults






if __name__ == '__main__':
    board = Board()
    for card in board.cards:
        print(card.name, end=',')
    print()

    player = 0
    while player == 0:
        board.print_status()
        





        player = 1 - player