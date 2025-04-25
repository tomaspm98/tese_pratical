import sys

def left_rotate(n, d):
    return (n << d | n >> (32 - d)) & 0xFFFFFFFF

def main():
    if len(sys.argv) != 3:
        print("Usage: python script.py <number> <rotation>")
        sys.exit(1)
    
    number = int(sys.argv[1])
    rotation = int(sys.argv[2])
    
    result = left_rotate(number, rotation)
    print(result)

if __name__ == "__main__":
    main()