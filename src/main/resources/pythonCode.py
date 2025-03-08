import sys

def add_integers(a, b):
    return a + b

def main():
    a = int(sys.argv[1])
    b = int(sys.argv[2])
    result = add_integers(a, b)
    print(result)

if __name__ == "__main__":
    main()