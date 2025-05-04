import sys

def find_perimeter(side):
    return 4 * side

def main():
    if len(sys.argv) != 2 or not sys.argv[1].isdigit():
        print("Usage: python script.py <side_length>")
        sys.exit(1)
    
    side = int(sys.argv[1])
    perimeter = find_perimeter(side)
    print(perimeter)

if __name__ == "__main__":
    main()