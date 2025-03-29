import sys

def find_perimeter(side_length):
    return 4 * side_length

def main():
    side_length = int(sys.argv[1])
    perimeter = find_perimeter(side_length)
    print(perimeter)

if __name__ == "__main__":
    main()