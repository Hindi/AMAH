package hexapode.markov;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import util.DataSaver;

/**
 * Contient la matrice de transition. Sérialisable afin d'avoir des données persistantes.
 * Changera à chaque changement de tests
 * @author pf
 *
 */

public class Markov implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private double matrice[][];
	private List<char[]> positionsViables;
	private Random randomgenerator = new Random();
	
	/**
	 * Constructeur pour une nouvelle matrice
	 * @param dimension
	 */
	public Markov(int nbEtatsParPattes)
	{
		// Dimension = nbEtatsParPattes^6
		int dimension = nbEtatsParPattes*nbEtatsParPattes*nbEtatsParPattes;
		dimension *= dimension;
		dimension = 64;
		matrice = new double[dimension][dimension];
		for(int i = 0; i < dimension; i++)
			for(int j = 0; j < dimension; j++)
				matrice[i][j] = 0;
		getPositionsViables();
	}
	
	private void getPositionsViables()
	{
		double pos[] = DataSaver.charger_matrice_equilibre("markov_equilibre.dat");
		positionsViables = new LinkedList<char[]>();
		for(int i=0; i< pos.length; i++)
		{
			if(pos[i] > 4)
			{
				positionsViables.add(Integer.toBinaryString(i).toCharArray());
				System.out.println(positionsViables.get(positionsViables.size()-1));
			}
		}
	}
	
	public String getRandomPositionViable()
	{
		/* Ce bloc permet de piocher une transition parmis les �tats d'�quilibres */
		int r = randomgenerator.nextInt(positionsViables.size());//Pioche un int entre 0 et 63
		String out = String.valueOf(positionsViables.get(r));

		while(out.length() < 6)
		{
			out = "0" + out;
		}
		return out;
	}
	
	public String next()
	{
		return getRandomPositionViable();
	}
	
	public String nextValidation(int numeroEtatActuel)
	{
		String out = new String();
		
		int lineSum = 0;
		for(int j = 0; j < matrice.length; ++j)
		{
			lineSum += matrice[numeroEtatActuel][j];
		}
		
		int r = randomgenerator.nextInt(lineSum+1);
		
		lineSum = 0;
		for(int j = 0; j < matrice.length; ++j)
		{
			lineSum += matrice[numeroEtatActuel][j];
			if(lineSum > r)
			{
				out = Integer.toBinaryString((int) matrice[numeroEtatActuel][j]);
				break;
			}
		}
        while(out.length() < 6)
        {
            out = "0" + out;
        }
		
		return out;
	}
	
	public void updateMatrix(int resultat, String etatPrecedent, String etatSuivant)
	{
		matrice[getNum(etatPrecedent)][getNum(etatSuivant)]+=resultat;
	}
	
	@Deprecated
	public void affiche_matrice()
	{
		String s = "";
		for(int i = 0; i < matrice.length; i++)
		{
			for(int j = 0; j < matrice.length; j++)
				s += Double.toString(matrice[i][j])+" ";
			s += "\n";
		}
		System.out.println(s);
	}
	
	public double[][] getMat()
	{
		return matrice;
	}
	
	public int getNum(String e)
	{
		int num = 0;
		for(int i = 0; i < 6; i++)
		{
			num *= 2;
			if(e.charAt(i) == '1')
				num++;
		}
		return num;
	}
	
	@Override
	public String toString()
	{
		String s = "";
		for(int i = 0; i < matrice.length; i++)
		{
			for(int j = 0; j < matrice.length; j++)
				s += Double.toString(matrice[i][j])+" ";
			s += "\n";
		}
		System.out.println(s);
		return s;
	}
	
}
