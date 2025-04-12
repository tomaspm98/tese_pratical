import math

def find_nth_tetrahedral(n):
    return int(math.comb(n + 2, 3))

def main():
    import sys
    if len(sys.argv) == 2:
        n = int(sys.argv[1])
        print(find_nth_tetrahedral(n))
    else:
        print("Usage: python script.py <integer>")

if __name__ == "__main__":
    main()