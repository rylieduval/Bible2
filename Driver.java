import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

public class Driver {
    public static void main(String[] args) 
    {
        long totalPopulation = 7700000000L;
        long year = 0;
        long christianPopulation = 13L;
        //create all the human groups
        ArrayList<HumanGroup> disciples = new ArrayList<>();
        ArrayList<HumanGroup> nonBelievers = new ArrayList<>();
        ArrayList<HumanGroup> apprentices = new ArrayList<>();
        //create first disciples
        HumanGroup firstDisciples = new HumanGroup();
        firstDisciples.Age = 30;
        firstDisciples.Population = 13;
        firstDisciples.IsDisciple = true;
        disciples.add(firstDisciples);
        
        HumanGroup otherHuman = new HumanGroup();
        otherHuman.Age = 18;
        otherHuman.Population = totalPopulation - christianPopulation;
        nonBelievers.add(otherHuman);

        while (christianPopulation < totalPopulation && !nonBelievers.isEmpty() && !disciples.isEmpty())
        {
            //see if all the groups have any people that are 72 or over
            deathCheck(disciples);
            deathCheck(apprentices);
            deathCheck(nonBelievers);
            
            //see how many new babies there are 
            calculateTotalBabyCount(disciples, apprentices, nonBelievers, year);
            //have each group age by 1 year
            ageGroups(disciples);
            ageGroups(apprentices);
            ageGroups(nonBelievers);
            //convert the non believers that have been taugh for 3 years to apprentices 
            graduateApprentices(apprentices, disciples);
            //calculate the total population to see if we continue 
            totalPopulation = calculateTotalPopulation(disciples, nonBelievers, apprentices);
            //increase the years by 1
            year++;
        }

        System.out.println("Took " + year + " years.");
    }
    //function to see if any member of the human groups hits the max age (72)
    private static void deathCheck(ArrayList<HumanGroup> peopleList) 
    {
        Iterator<HumanGroup> iterator = peopleList.iterator();
        while (iterator.hasNext()) 
        {
            HumanGroup person = iterator.next();
            if (person.Age >= Human.MaxAge) 
            {
                iterator.remove();
            }
        }
    }
    //function to see the new babies 
    private static void calculateTotalBabyCount(ArrayList<HumanGroup> disciples, ArrayList<HumanGroup> apprentices, ArrayList<HumanGroup> nonBelievers, long year) {
        long totalBaby = 0;

        for (HumanGroup group : disciples) 
        {
            if (group.Age == Human.AgeOfGivingBirth && year > 0) 
            {
                totalBaby += group.Population;
            }
        }

        for (HumanGroup group : apprentices)
        {
            if (group.Age == Human.AgeOfGivingBirth) 
            {
                totalBaby += group.Population;
            }
        }

        for (HumanGroup group : nonBelievers) 
        {
            if (group.Age == Human.AgeOfGivingBirth) 
            {
                totalBaby += group.Population;
            }
        }
        
        if (totalBaby > 0) {
            HumanGroup baby = new HumanGroup();
            baby.Population = totalBaby;
            nonBelievers.add(baby);
        }
    }
    //function to convert nonbelievers to apprentices
    private static void convertNonBelievers(ArrayList<HumanGroup> nonBelievers, ArrayList<HumanGroup> apprentices, ArrayList<HumanGroup> disciples) {
        long totalNewApprentice = 0;
        //calculate the new apprentices
        for (HumanGroup group : disciples) {
            if (group.YearsInTeaching == 0) {
                totalNewApprentice += group.Population * Human.CapOfApprentice;
            }
        }

        Collections.sort(nonBelievers, HumanGroup.ageAscComparator);
        
        Iterator<HumanGroup> getNonBeliever = nonBelievers.iterator();
        //convert new non-believers to apprentices
        while (getNonBeliever.hasNext() && totalNewApprentice > 0) {
            HumanGroup spot = getNonBeliever.next();
            if (spot.Age >= Human.AgeAsApprentice) 
            {
                HumanGroup newApprentice = spot.Clone();
                if (spot.Population <= totalNewApprentice) 
                {
                    getNonBeliever.remove();
                    totalNewApprentice -= spot.Population;
                } 
                else 
                {
                    spot.Population -= totalNewApprentice;
                    newApprentice.Population = totalNewApprentice;
                    totalNewApprentice = 0;
                }
                    
                apprentices.add(newApprentice);
            }
        }
    }
    //function to age all the groups by 1 year 
    private static void ageGroups(ArrayList<HumanGroup> groupList) 
    {
        for (HumanGroup group : groupList) 
        {
            group.Aging();
        }
    }
    //make apprentices into desciples
    private static void graduateApprentices(ArrayList<HumanGroup> apprentices, ArrayList<HumanGroup> disciples) {
        Iterator<HumanGroup> getApprentice = apprentices.iterator();
        while (getApprentice.hasNext()) 
        {
            HumanGroup spot = getApprentice.next();
            if (spot.YearsAsApprentice == Human.YearToBeDisciple) 
            {
                spot.YearsAsApprentice = 0;
                spot.IsApprentice = false;
                spot.IsDisciple = true;
                getApprentice.remove();
                disciples.add(spot);
            }
        }
    }
    
    private static long calculateTotalPopulation(ArrayList<HumanGroup> disciples, ArrayList<HumanGroup> nonBelievers, ArrayList<HumanGroup> apprentices) {
        long total = 0;

        for (HumanGroup group : disciples) {
            total += group.Population;
        }

        for (HumanGroup group : nonBelievers) {
            total += group.Population;
        }

        for (HumanGroup group : apprentices) {
            total += group.Population;
        }

        return total;
    }
}
