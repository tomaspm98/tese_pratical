import sys

def find_min(num1, num2, num3):
    return min(num1, num2, num3)

def main():
    if len(sys.argv) != 4:
        print("Usage: python script.py <num1> <num2> <num3>")
        sys.exit(1)
    
    try:
        num1 = int(sys.argv[1])
        num2 = int(sys.argv[2])
        num3 = int(sys.argv[3])
    except ValueError:
        print("All inputs must be integers.")
        sys.exit(1)
    
    min_num = find_min(num1, num2, num3)
    print(min_num)

if __name__ == "__main__":
    main()