import sys

def opposite_Signs(x, y):
    return (x * y) < 0

def main():
    x = int(sys.argv[1])
    y = int(sys.argv[2])
    result = opposite_Signs(x, y)
    print(result)

if __name__ == "__main__":
    main()