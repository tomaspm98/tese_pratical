def calculate_perimeter(side):
    return 4 * side

def main():
    import sys
    if len(sys.argv) > 1:
        side = int(sys.argv[1])
        perimeter = calculate_perimeter(side)
        print(perimeter)

if __name__ == "__main__":
    main()