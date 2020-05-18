import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class PatternSearch {

	static void Naive(String pat, String txt, int nr) {
		int m = pat.length();
		int n = txt.length();
		int j;
		for (int i = 0; i <= n - m; i++) {
			for (j = 0; j < m; j++)
				if (txt.charAt(i + j) != pat.charAt(j))
					break;
			if (j == m)
				System.out.println(String.format("NAIWNY wiersz: %d pozycja: %d", nr, i + 1));
		}
	}

	static void rabinKarp(String pat, String txt, int q, int nr) {
		int d = 128; //rozmiar alfabetu
		int m = pat.length();
		int n = txt.length();

		int h = 1;
		int i, j;

		for (i = 0; i < m - 1; i++)
			h = (h * d) % q;

		int p = 0;
		int t = 0;
		for (i = 0; i < m; i++) { //haszowanie
			p = (d * p + pat.charAt(i)) % q;
			t = (d * t + txt.charAt(i)) % q;
		}

		for (i = 0; i <= n - m; i++) {
			if (p == t) {
				for (j = 0; j < m; j++) {
					if (txt.charAt(i + j) != pat.charAt(j))
						break;
				}

				if (j == m)
					System.out.println(String.format("RABIN KARP wiersz: %d pozycja: %d", nr, i + 1));
			}

			if (i < n - m) { //sprawdzam czy koniec tekstu
				t = (d * (t - txt.charAt(i) * h) + txt.charAt(i + m)) % q;
				if (t < 0)
					t = (t + q);
			}
		}
	}

	public static void main(String[] args) {

		Scanner scanner;

		//-----------------------------------------------------------------
		// ³adowanie wzorca z pliku
		String wzorzec = "";
		try {
			scanner = new Scanner( // proszê o podanie w³asnej œcie¿ki
					new File("C:\\Users\\junes\\eclipse-workspace\\Szukanie wzorca algo\\src\\wzorzec.txt"));
			while (scanner.hasNext()) {
				String line = scanner.next();
				wzorzec += line + "\n";
			}
		} catch (FileNotFoundException e) {
			System.out.println("Nie uda³o siê za³adowanie pliku wzorzec.txt");
			e.printStackTrace();
		}
		int wzorzecLength = wzorzec.length();

		//-----------------------------------------------------------------
		// ³adowanie tekstu z pliku
		String tekst = "";
		try {
			scanner = new Scanner( // proszê o podanie w³asnej œcie¿ki
					new File("C:\\Users\\junes\\eclipse-workspace\\Szukanie wzorca algo\\src\\tekst.txt"));
			while (scanner.hasNext()) {
				String line = scanner.next();
				tekst += line + "\n";
			}
		} catch (FileNotFoundException e) {
			System.out.println("Nie uda³o siê za³adowanie pliku tekst.txt");
			e.printStackTrace();
		}

		//-----------------------------------------------------------------
		// sposób na ignorowanie znaków nowej linii przy jednoczesnym zachowaniu numeracji wierszy
		int lineNumber = 1; //numer wiersza
		int startOfLine = 0; //pocz¹tek wiersza
		String currentString = ""; //wycinek z tekstu podawany do funkcji
		int lineLength = 0; //d³ugoœæ wiersza
		wzorzec = wzorzec.replace("\n", ""); //wzorzec bez znaków nowej linii
		for (int i = 0; i < tekst.length() - wzorzecLength - 1; i++) {
			if (tekst.charAt(i) == '\n') {
				lineLength = i - startOfLine; // wzorzec mo¿e wystêpowaæ w danym wierszu w zakresie od wiersz + nastepne (dlugosc wzorca-1) znakow ->
												// -> (przypadek kiedy wzorzec zaczyna siê na ostatniej literze wiersza)
				currentString = tekst.substring(startOfLine, startOfLine + lineLength + wzorzecLength - 1).replace("\n", "");
				startOfLine = i + 1;
				// start
				Naive(wzorzec, currentString, lineNumber);
				rabinKarp(wzorzec, currentString, 27077, lineNumber);
				// stop
				// potem suma tych czasow
				lineNumber++;
			}

		}

	}

}
