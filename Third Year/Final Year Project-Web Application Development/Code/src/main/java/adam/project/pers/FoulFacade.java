/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adam.project.pers;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import adam.project.ents.Foul;

/**
 *
 * @author adamt
 */
@Stateless
public class FoulFacade extends AbstractFacade<Foul> {

    @PersistenceContext(unitName = "adamPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FoulFacade() {
        super(Foul.class);
    }
    
}
