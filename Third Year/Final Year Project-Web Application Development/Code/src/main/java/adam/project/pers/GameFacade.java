/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adam.project.pers;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import adam.project.ents.Game;

/**
 *
 * @author adamt
 */
@Stateless
public class GameFacade extends AbstractFacade<Game> {

    @PersistenceContext(unitName = "adamPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GameFacade() {
        super(Game.class);
    }
    
}
