package daa;

public class DikjstraAlgorithm {
    public static void main(String[] args) {
    	/*
        Graph graph = new Graph(10);
        for (int i = 1; i < 10; i++) {
            graph.addVertex(i);
        }
        graph.addEdge(1, 2, 4);
        graph.addEdge(1, 8, 8);
        graph.addEdge(2, 1, 4);
        graph.addEdge(2, 8, 11);
        graph.addEdge(2, 3, 8);
        graph.addEdge(3, 2, 8);
        graph.addEdge(3, 4, 7);
        graph.addEdge(3, 9, 2);
        graph.addEdge(3, 6, 4);
        graph.addEdge(4, 3, 7);
        graph.addEdge(4, 5, 9);
        graph.addEdge(4, 6, 14);
        graph.addEdge(5, 4, 9);
        graph.addEdge(5, 6, 10);
        graph.addEdge(6, 3, 4);
        graph.addEdge(6, 4, 14);
        graph.addEdge(6, 5, 10);
        graph.addEdge(6, 7, 2);
        graph.addEdge(7, 6, 2);
        graph.addEdge(7, 8, 1);
        graph.addEdge(7, 9, 6);
        graph.addEdge(8, 1, 8);
        graph.addEdge(8, 2, 11);
        graph.addEdge(8, 7, 1);
        graph.addEdge(8, 9, 7);
        graph.addEdge(9, 3, 2);
        graph.addEdge(9, 7, 6);
        graph.addEdge(9, 8, 7);
        graph.findShortestPaths(1);
        */
    }    
    public static class Graph {
        Vertex[] vertices;
        int maxSize;
        int size=1;

        public Graph(int maxSize) {
            this.maxSize = maxSize;
            vertices = new Vertex[maxSize];
        }

        public void addVertex(int name) {
            vertices[size++] = new Vertex(name);
        }

        public void addEdge(int sourceName, int destinationName, double weight) {
            int srcIndex = sourceName;
            int destiIndex = destinationName;
            vertices[srcIndex].adj = new Neighbour(destiIndex, weight, vertices[srcIndex].adj);
            vertices[destiIndex].indegree++;
        }
        
        public void findShortestPaths(int sourceName){
            applyDikjstraAlgorith(vertices[sourceName]);
            System.out.println("S D C Path");
            for(int i = 1; i < maxSize; i++){
                //System.out.println("Distance of "+vertices[i].name+" from Source: "+ vertices[i].cost);
                System.out.println(sourceName+" " + vertices[i].name+" "+ vertices[i].cost+" "+vertices[i].path);
            }
        }
        public String findShortestPaths(int sourceName,int destinationName){
            applyDikjstraAlgorith(vertices[sourceName]);
            System.out.println("S D C Path");
            //System.out.println("Shortest Path from Source "+vertices[sourceName].name +" to Destination "+vertices[destinationName].name+" is "+ vertices[destinationName].cost);
            System.out.println(sourceName+" " + vertices[destinationName].name+" "+ vertices[destinationName].cost+" "+vertices[destinationName].path);
            //path
            //System.out.println(vertices[destinationName].path);
            return vertices[destinationName].path;
        }
        
        public class Vertex {
            double cost;
            int name;
            Neighbour adj;
            int indegree;
            State state;
            String path="";

            public Vertex(int name) {
                this.name = name;
                cost = Double.MAX_VALUE;
                state = State.NEW;
            }

            public int compareTo(Vertex v) {
                if (this.cost == v.cost) {
                    return 0;
                }
                if (this.cost < v.cost) {
                    return -1;
                }
                return 1;
            }
        }

        public enum State {
            NEW, IN_Q, VISITED
        }

        public class Neighbour {
            int index;
            Neighbour next;
            double weight;

            Neighbour(int index, double weight, Neighbour next) {
                this.index = index;
                this.next = next;
                this.weight = weight;
            }
        }

        public void applyDikjstraAlgorith(Vertex src) {
            Heap heap = new Heap(maxSize);
            heap.add(src);
            src.state = State.IN_Q;
            src.cost = 0;
            while (!heap.isEmpty()) {
                Vertex u = heap.remove();
                u.state = State.VISITED;
                Neighbour temp = u.adj;
                while (temp != null) {
                    if (vertices[temp.index].state == State.NEW) {
                        heap.add(vertices[temp.index]);
                        vertices[temp.index].state = State.IN_Q;
                    }
                    if (vertices[temp.index].cost > u.cost + temp.weight) {
                        vertices[temp.index].cost = u.cost + temp.weight;
                        heap.heapifyUP(vertices[temp.index]);
                        //path
                        vertices[temp.index].path=u.path+u.name+","+temp.index+";";
                    }
                    temp = temp.next;
                }
            }
        }

        public static class Heap {
            private Vertex[] heap;
            private int maxSize;
            private int size;

            public Heap(int maxSize) {
                this.maxSize = maxSize;
                heap = new Vertex[maxSize];
            }

            public void add(Vertex u) {
                heap[size++] = u;
                heapifyUP(size - 1);
            }

            public void heapifyUP(Vertex u) {
                for (int i = 0; i < maxSize; i++) {
                    if (u == heap[i]) {
                        heapifyUP(i);
                        break;
                    }
                }
            }

            public void heapifyUP(int position) {
                int currentIndex = position;
                Vertex currentItem = heap[currentIndex];
                int parentIndex = (currentIndex - 1) / 2;
                Vertex parentItem = heap[parentIndex];
                while (currentItem.compareTo(parentItem) == -1) {
                    swap(currentIndex, parentIndex);
                    currentIndex = parentIndex;
                    if (currentIndex == 0) {
                        break;
                    }
                    currentItem = heap[currentIndex];
                    parentIndex = (currentIndex - 1) / 2;
                    parentItem = heap[parentIndex];
                }
            }

            public Vertex remove() {
                Vertex v = heap[0];
                swap(0, size - 1);
                heap[size - 1] = null;
                size--;
                heapifyDown(0);
                return v;
            }

            public void heapifyDown(int postion) {
                if (size == 1) {
                    return;
                }

                int currentIndex = postion;
                Vertex currentItem = heap[currentIndex];
                int leftChildIndex = 2 * currentIndex + 1;
                int rightChildIndex = 2 * currentIndex + 2;
                int childIndex;
                if (heap[leftChildIndex] == null) {
                    return;
                }
                if (heap[rightChildIndex] == null) {
                    childIndex = leftChildIndex;
                } else if (heap[rightChildIndex].compareTo(heap[leftChildIndex]) == -1) {
                    childIndex = rightChildIndex;
                } else {
                    childIndex = leftChildIndex;
                }
                Vertex childItem = heap[childIndex];
                while (currentItem.compareTo(childItem) == 1) {
                    swap(currentIndex, childIndex);
                    currentIndex = childIndex;
                    currentItem = heap[currentIndex];
                    leftChildIndex = 2 * currentIndex + 1;
                    rightChildIndex = 2 * currentIndex + 2;
                    if (heap[leftChildIndex] == null) {
                        return;
                    }
                    if (heap[rightChildIndex] == null) {
                        childIndex = leftChildIndex;
                    } else if (heap[rightChildIndex].compareTo(heap[leftChildIndex]) == -1) {
                        childIndex = rightChildIndex;
                    } else {
                        childIndex = leftChildIndex;
                    }
                }
            }

            public void swap(int index1, int index2) {
                Vertex temp = heap[index1];
                heap[index1] = heap[index2];
                heap[index2] = temp;
            }

            public boolean isEmpty() {

                return size == 0;
            }
        }
    }
}
