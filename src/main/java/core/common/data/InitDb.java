package core.common.data;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import core.join.entity.JoinEntity;
import core.join.entity.RequesterType;
import core.join.entity.StatusType;
import core.player.entity.BelongType;
import core.player.entity.PlayerEntity;
import core.team.entity.TeamEntity;
import lombok.RequiredArgsConstructor;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitDb {
	
	private final InitService initService;
	
	@PostConstruct
	public void init() {
		initService.dbInit1();
	}
	
	@Component
	@Transactional
	@RequiredArgsConstructor
	static class InitService{
		private final EntityManager em;
		public void dbInit1() {
			TeamEntity team1 = new TeamEntity("redTeam","Busan",BelongType.CLUB,"We are Red team");
			TeamEntity team2 = new TeamEntity("greenTeam","Wonju",BelongType.UNIVERSITY,"We are Green team");
			TeamEntity team3 = new TeamEntity("purpleTeam","Seoul",BelongType.CLUB,"We are Purple team");
			TeamEntity team4 = new TeamEntity("yellowTeam","Suwon",BelongType.PROTEAM,"We are Yellow team");
			TeamEntity team5 = new TeamEntity("blueTeam","Suwon",BelongType.PROTEAM,"We are Blue team");
			TeamEntity team6 = new TeamEntity("orangeTeam","Suwon",BelongType.UNIVERSITY,"We are Orange team");
			em.persist(team1);
			em.persist(team2);
			em.persist(team3);
			em.persist(team4);
			em.persist(team5);
			em.persist(team6);
			
			PlayerEntity player1 = new PlayerEntity("apple","220619-1111111",1,team1);
			PlayerEntity player2 = new PlayerEntity("bean","220619-1111111",2,team1);
			PlayerEntity player3 = new PlayerEntity("cat","220619-1111111",3,team1);
			PlayerEntity player4 = new PlayerEntity("dog","220619-1111111",4,team1);
			PlayerEntity player5 = new PlayerEntity("elephant","220619-1111111",5,team1);
			PlayerEntity player6 = new PlayerEntity("fox","220619-1111111",6,team1);
			PlayerEntity player7 = new PlayerEntity("gorilla","220619-1111111",7,team1);
			PlayerEntity player8 = new PlayerEntity("hippo","220619-1111111",8,team1);
			PlayerEntity player9 = new PlayerEntity("iguana","220619-1111111",9,team1);
			PlayerEntity player10 = new PlayerEntity("kangaroo","220619-1111111",10,team1);
			em.persist(player1);
			em.persist(player2);
			em.persist(player3);
			em.persist(player4);
			em.persist(player5);
			em.persist(player6);
			em.persist(player7);
			em.persist(player8);
			em.persist(player9);
			em.persist(player10);
			
			JoinEntity join1 = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,player2,team2);
			JoinEntity join2 = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,player2,team4);
			JoinEntity join3 = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,player2,team6);
			JoinEntity join4 = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,player3,team3);
			JoinEntity join5 = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,player3,team6);
			JoinEntity join6 = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,player4,team4);
			JoinEntity join7 = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,player5,team5);
			JoinEntity join8 = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,player6,team6);
			JoinEntity join9 = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,player8,team4);
			JoinEntity join10 = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,player8,team2);
			JoinEntity join11 = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,player9,team3);
			JoinEntity join12 = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,player10,team5);
			JoinEntity join13 = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,player10,team2);
			
			em.persist(join1);
			em.persist(join2);
			em.persist(join3);
			em.persist(join4);
			em.persist(join5);
			em.persist(join6);
			em.persist(join7);
			em.persist(join8);
			em.persist(join9);
			em.persist(join10);
			em.persist(join11);
			em.persist(join12);
			em.persist(join13);
			
			JoinEntity join14 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player1,team2);
			JoinEntity join15 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player3,team2);
			JoinEntity join16 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player7,team2);
			JoinEntity join17 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player9,team2);
			JoinEntity join18 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player2,team3);
			JoinEntity join19 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player4,team3);
			JoinEntity join20 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player5,team3);
			JoinEntity join21 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player7,team3);
			JoinEntity join22 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player8,team3);
			JoinEntity join23 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player10,team3);
			JoinEntity join24 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player3,team4);
			JoinEntity join25 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player5,team4);
			JoinEntity join26 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player6,team4);
			JoinEntity join27 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player7,team4);
			JoinEntity join28 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player9,team4);
			JoinEntity join29 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player10,team4);
			JoinEntity join30 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player2,team5);
			JoinEntity join31 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player3,team5);
			JoinEntity join32 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player4,team5);
			JoinEntity join33 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player6,team5);
			JoinEntity join34 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player7,team5);
			JoinEntity join35 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player8,team5);
			JoinEntity join36 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player9,team5);
			JoinEntity join37 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player4,team6);
			JoinEntity join38 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player5,team6);
			JoinEntity join39 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player7,team6);
			JoinEntity join40 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player8,team6);
			JoinEntity join41 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player9,team6);
			JoinEntity join42 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player10,team6);
			
			em.persist(join14);
			em.persist(join15);
			em.persist(join16);
			em.persist(join17);
			em.persist(join18);
			em.persist(join19);
			em.persist(join20);
			em.persist(join21);
			em.persist(join22);
			em.persist(join23);
			em.persist(join24);
			em.persist(join25);
			em.persist(join26);
			em.persist(join27);
			em.persist(join28);
			em.persist(join29);
			em.persist(join30);
			em.persist(join31);
			em.persist(join32);
			em.persist(join33);
			em.persist(join34);
			em.persist(join35);
			em.persist(join36);
			em.persist(join37);
			em.persist(join38);
			em.persist(join39);
			em.persist(join40);
			em.persist(join41);
			em.persist(join42);
		}
	}
}
