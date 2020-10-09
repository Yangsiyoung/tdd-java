package programmers;

public class Solution49993 {
    public int solution(String skill, String[] skillTrees) {
        int answer = 0;
        for(String skillTree : skillTrees) {
            if(isValidSkillTree(skill, skillTree)) {
                answer++;
            }
        }
        return answer;
    }

    public boolean isValidSkillTree(String skill, String skillTree) {
        int requiredSkillIndex = -1;
        boolean result = true;
        for(int indexOfSkill = 0; indexOfSkill < skill.length(); indexOfSkill++) {
            char requiredSkill = skill.charAt(indexOfSkill);
            int skillIndex = skillTree.indexOf(requiredSkill);
            if(skillIndex > requiredSkillIndex) {
                requiredSkillIndex = skillIndex;
            } else if(skillIndex == -1) {
                requiredSkillIndex = Integer.MAX_VALUE;
                continue;
            } else {
                result = false;
                break;
            }
        }
        return result;
    }
}
