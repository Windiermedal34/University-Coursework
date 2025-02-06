/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package adam.project.bus;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import adam.project.ents.Foul;
import adam.project.ents.Game;
import adam.project.ents.FoulCodes;
import adam.project.pers.FoulFacade;
import adam.project.pers.GameFacade;
import adam.project.pers.FoulCodesFacade;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author adamt
 */
@Stateless
public class StartService {

    @EJB
    private FoulFacade ff;

    @EJB
    private FoulCodesFacade fcf;

    @EJB
    private GameFacade gf;

    public Foul addFoul(Foul foul, Game game) {
        foul.setGame(game);
        List<Foul> fouls = retrieveFoul();
        fouls.add(foul);
        game.setFoulList(fouls);
        gf.edit(game);
        return foul;
    }

    public void populateFoulCodeTable(int i) {
        FoulCodes nfc = new FoulCodes();
        List<String> fouls = null;
        List<String> codes = null;
        fouls = populateNameList(fouls);
        codes = populateCodeList(codes);
        String foul = fouls.get(i);
        String code = codes.get(i);
        nfc.setFoulCode(code);
        nfc.setFoulName(foul);
        fcf.create(nfc);
    }

    public void delete(long records) {
        Foul foul = ff.find(records);
        Game game = gf.find(foul.getGame().getId());
        List<Foul> gameFouls = game.getFoulList();
        gameFouls.remove(foul);
        game.setFoulList(gameFouls);
        gf.edit(game);
        ff.remove(foul);
    }

    public void edit(Foul foul, Game g, long records) {
        Foul f = ff.find(records);
        f.setGame(g);
        f.setQuarter(foul.getQuarter());
        f.setTime(foul.getTime());
        f.setHomeOrAway(foul.getHomeOrAway());
        f.setFoulName(foul.getFoulName());
        f.setPlayer(foul.getPlayer());
        f.setPosition(foul.getPosition());
        f.setOfficials(foul.getOfficials());
        f.setDecision(foul.getDecision());
        g = gf.find(f.getGame().getId());
        List<Foul> gameFouls = g.getFoulList();
        int index = 0;
        for (int i = 0; i < gameFouls.size(); i++) {
            if (gameFouls.get(i).getId().equals(f.getId())) {
                break;
            }
            index++;
        }
        gameFouls.set(index, f);
        g.setFoulList(gameFouls);

        gf.edit(g);
        ff.edit(f);
    }

    public Map<String, String> populatePositions(Map<String, String> positions) {
        positions = new LinkedHashMap<>();
        positions.put("","");
        positions.put("C", "C");
        positions.put("CB", "CB");
        positions.put("DB", "DB");
        positions.put("DE", "DE");
        positions.put("DL", "DL");
        positions.put("DT", "DT");
        positions.put("FB", "FB");
        positions.put("FS", "FS");
        positions.put("G", "G");
        positions.put("ILB", "ILB");
        positions.put("K", "K");
        positions.put("KR", "KR");
        positions.put("LB", "LB");
        positions.put("LG", "LG");
        positions.put("LT", "LT");
        positions.put("MLB", "MLB");
        positions.put("NT", "NT");
        positions.put("OL", "OL");
        positions.put("OLB", "OLB");
        positions.put("P", "P");
        positions.put("PR", "PR");
        positions.put("QB", "QB");
        positions.put("RB", "RB");
        positions.put("RG", "RG");
        positions.put("RT", "RT");
        positions.put("S", "S");
        positions.put("SS", "SS");
        positions.put("T", "T");
        positions.put("TE", "TE");
        positions.put("WR", "WR");
        return positions;
    }

    public List<Foul> GetFoulsAdded(List<Foul> foulsAdded) {
        List<Foul> foulRetrieved = retrieveFoul();
        for (int i = foulRetrieved.size() - 1; i >= 0; i--) {
            foulsAdded.add(foulRetrieved.get(i));
        }
        return foulsAdded;
    }

    public List<Foul> retrieveFoul() {
        List<Foul> fouls = ff.findAll();
        return fouls;
    }

    public List<String> populateCodeList(List<String> codes) {
        codes = new ArrayList<>();
        codes.add("APS");
        codes.add("ATR");
        codes.add("BAT");
        codes.add("DEH");
        codes.add("DOD");
        codes.add("DOF");
        codes.add("DOG");
        codes.add("DPI");
        codes.add("DSH");
        codes.add("DSQ");
        codes.add("ENC");
        codes.add("FGT");
        codes.add("FST");
        codes.add("IBB");
        codes.add("IBK");
        codes.add("IBP");
        codes.add("IDP");
        codes.add("IFD");
        codes.add("IFH");
        codes.add("IFK");
        codes.add("IFP");
        codes.add("IJY");
        codes.add("IKB");
        codes.add("ILF");
        codes.add("ILM");
        codes.add("ING");
        codes.add("IPN");
        codes.add("IPR");
        codes.add("ISH");
        codes.add("ISP");
        codes.add("ITP");
        codes.add("IUH");
        codes.add("IWK");
        codes.add("KCI");
        codes.add("KIK");
        codes.add("KOB");
        codes.add("OBK");
        codes.add("OFH");
        codes.add("OFK");
        codes.add("OPI");
        codes.add("PF-BBW");
        codes.add("PF-BOB");
        codes.add("PF-BTH");
        codes.add("PF-CHB");
        codes.add("PF-CLP");
        codes.add("PF-FMM");
        codes.add("PF-HCT");
        codes.add("PF-HDR");
        codes.add("PF-HTF");
        codes.add("PF-HUR");
        codes.add("PF-ICS");
        codes.add("PF-LEA");
        codes.add("PF-LHP");
        codes.add("PF-LTO");
        codes.add("PF-OTH");
        codes.add("PF-RFK");
        codes.add("PF-RTH");
        codes.add("PF-RTK");
        codes.add("PF-RTP");
        codes.add("PF-SKE");
        codes.add("PF-TGT");
        codes.add("PF-TRP");
        codes.add("PF-UNR");
        codes.add("RNH");
        codes.add("RNK");
        codes.add("SLI");
        codes.add("SLM");
        codes.add("SLW");
        codes.add("SUB");
        codes.add("UC-2PN");
        codes.add("UC-ABL");
        codes.add("UC-BCH");
        codes.add("UC-DEA");
        codes.add("UC-FCO");
        codes.add("UC-RHT");
        codes.add("UC-SBR");
        codes.add("UC-STB");
        codes.add("UC-TAU");
        codes.add("UC-UNS");
        codes.add("UFA");
        codes.add("UFT");
        return codes;
    }

    public List<String> populateNameList(List<String> names) {
        names = new ArrayList<>();
        names.add("Altering playing surface");
        names.add("Assisting the runner");
        names.add("Illegal batting");
        names.add("Holding, defense");
        names.add("Delay of game, defense");
        names.add("Offside, defense");
        names.add("Delay of game, offense");
        names.add("Pass interference, defense");
        names.add("Delay of game, start of half");
        names.add("Disqualification");
        names.add("Encroachment (offense)");
        names.add("Fighting");
        names.add("False start");
        names.add("Illegal block in the back");
        names.add("Illegal block during kick");
        names.add("Illegal backward pass");
        names.add("Ineligible downfield on pass");
        names.add("Illegal formation, defense (3-on-1)");
        names.add("Illegal forward handling");
        names.add("Illegal free kick formation");
        names.add("Illegal forward pass");
        names.add("Illegal jerseys");
        names.add("Illegally kicking ball");
        names.add("Illegal formation");
        names.add("Illegal motion");
        names.add("Intentional grounding");
        names.add("Improper numbering");
        names.add("Illegal procedure");
        names.add("Illegal shift");
        names.add("Illegal snap");
        names.add("Illegal touching of a forward pass");
        names.add("Illegal use of hands");
        names.add("Illegal wedge on kickoff");
        names.add("Kick-catch interference");
        names.add("Illegal kick");
        names.add("Free kick out of bounds");
        names.add("Out of bounds during kick");
        names.add("Holding, offense");
        names.add("Offside, free kick");
        names.add("Pass interference, offense");
        names.add("Personal Foul, Blocking Below the Waist");
        names.add("Personal Foul, Blocking Out of Bounds");
        names.add("Personal Foul, Blow To the Head");
        names.add("Personal Foul, Chop Block");
        names.add("Personal Foul, Clipping");
        names.add("Personal Foul, Face Mask");
        names.add("Personal Foul, Horse Collar Tackle");
        names.add("Personal Foul, Hit on Defenseless Receiver");
        names.add("Personal Foul, Hands To the Face");
        names.add("Personal Foul, Hurdling");
        names.add("Personal Foul, Illegal Contact with Snapper");
        names.add("Personal Foul, Leaping");
        names.add("Personal Foul, Late Hit/Piling on");
        names.add("Personal Foul, Late Hit out of Bounds");
        names.add("Personal Foul, Other");
        names.add("Personal Foul, Roughing Free Kicker");
        names.add("Personal Foul, Roughing The Holder");
        names.add("Personal Foul, Roughing The Kicker");
        names.add("Personal Foul, Roughing The Passer");
        names.add("Personal Foul, Striking/Kneeing/Elbowing");
        names.add("Personal Foul, Targeting");
        names.add("Personal Foul, Tripping");
        names.add("Personal Foul, Unnecessary Roughness");
        names.add("Running into the holder");
        names.add("Running into the kicker");
        names.add("Sideline interference, 5 yards");
        names.add("Sideline interference, 15 yards");
        names.add("Sideline interference, warning");
        names.add("Illegal substitution");
        names.add("Unsportsmanlike conduct, two players with same number");
        names.add("Unsportsmanlike conduct, abusive language");
        names.add("Unsportsmanlike conduct, bench");
        names.add("Unsportsmanlike conduct, delayed/excessive act");
        names.add("Unsportsmanlike conduct, forcibly contacting an official");
        names.add("Unsportsmanlike conduct, removal of helmet");
        names.add("Unsportsmanlike conduct, simulating being roughed");
        names.add("Unsportsmanlike conduct, spiking/throwing ball");
        names.add("Unsportsmanlike conduct, taunting/baiting");
        names.add("Unsportsmanlike conduct, other");
        names.add("Unfair acts");
        names.add("Unfair tactics");
        return names;
    }
}
