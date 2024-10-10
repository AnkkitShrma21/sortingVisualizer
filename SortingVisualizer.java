import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class SortingVisualizer extends JPanel {

    private int[] array;
    private int delay = 100; // Delay in milliseconds for the animation
    private String algorithm = "Bubble Sort"; // Default sorting algorithm

    public SortingVisualizer(int[] array) {
        this.array = array;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.BLACK);

        int width = getWidth();
        int height = getHeight();
        int barWidth = width / array.length;

        for (int i = 0; i < array.length; i++) {
            int barHeight = (int) ((array[i] / (double) Arrays.stream(array).max().getAsInt()) * height);
            g.setColor(Color.CYAN);
            g.fillRect(i * barWidth, height - barHeight, barWidth, barHeight);
        }
    }

    public void bubbleSort() throws InterruptedException {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    // Swap the elements
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;

                    // Repaint the panel to update the array visualization
                    repaint();
                    Thread.sleep(delay);
                }
            }
        }
    }

    public void selectionSort() throws InterruptedException {
        for (int i = 0; i < array.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < array.length; j++) {
                if (array[j] < array[minIndex]) {
                    minIndex = j;
                }
            }
            // Swap the found minimum element with the first element
            int temp = array[minIndex];
            array[minIndex] = array[i];
            array[i] = temp;

            // Repaint the panel to update the array visualization
            repaint();
            Thread.sleep(delay);
        }
    }

    public void insertionSort() throws InterruptedException {
        for (int i = 1; i < array.length; i++) {
            int key = array[i];
            int j = i - 1;

            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;

            // Repaint the panel to update the array visualization
            repaint();
            Thread.sleep(delay);
        }
    }

    public void startSorting() {
        new Thread(() -> {
            try {
                switch (algorithm) {
                    case "Bubble Sort":
                        bubbleSort();
                        break;
                    case "Selection Sort":
                        selectionSort();
                        break;
                    case "Insertion Sort":
                        insertionSort();
                        break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Sorting Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Generate random array for sorting
        int[] array = new int[50];
        for (int i = 0; i < array.length; i++) {
            array[i] = (int) (Math.random() * 500 + 1);
        }

        SortingVisualizer visualizer = new SortingVisualizer(array);
        frame.add(visualizer, BorderLayout.CENTER);

        // Dropdown for selecting sorting algorithm
        String[] algorithms = {"Bubble Sort", "Selection Sort", "Insertion Sort"};
        JComboBox<String> algorithmSelector = new JComboBox<>(algorithms);
        algorithmSelector.setSelectedItem(visualizer.algorithm);
        algorithmSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                visualizer.setAlgorithm((String) algorithmSelector.getSelectedItem());
            }
        });

        // Button to start sorting
        JButton sortButton = new JButton("Sort");
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Reset the array with random values
                for (int i = 0; i < array.length; i++) {
                    array[i] = (int) (Math.random() * 500 + 1);
                }
                visualizer.repaint();
                visualizer.startSorting(); // Start sorting on a new thread
            }
        });

        JPanel controlPanel = new JPanel();
        controlPanel.add(algorithmSelector);
        controlPanel.add(sortButton);
        frame.add(controlPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}
