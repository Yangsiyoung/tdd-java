package programmers;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Solution49993Test  {

    private Solution49993 solution49993;

    @Before
    public void init() {
        solution49993 = new Solution49993();
    }

    @Test
    public void testSolution() {
        String skill = "CBD";
        String[] skillTrees = {"BACDE", "CBADF", "AECB", "BDA"};
        int expect = 2;
        int result = solution49993.solution(skill, skillTrees);
    }
    @Test
    public void testIsValidSkillTree() {
        String skill = "CBD";
        String skillTree = "BACDE";
        boolean result = solution49993.isValidSkillTree(skill, skillTree);
        assertFalse(result);
        skillTree = "CBADF";
        result = solution49993.isValidSkillTree(skill, skillTree);
        assertTrue(result);
        skillTree = "BDA";
        result = solution49993.isValidSkillTree(skill, skillTree);
        assertFalse(result);
        skillTree = "B";
        result = solution49993.isValidSkillTree(skill, skillTree);
        assertFalse(result);
        skillTree = "C";
        result = solution49993.isValidSkillTree(skill, skillTree);
        assertTrue(result);
        skillTree = "AEFGHIJKLMN";
        result = solution49993.isValidSkillTree(skill, skillTree);
        assertTrue(result);
    }

}