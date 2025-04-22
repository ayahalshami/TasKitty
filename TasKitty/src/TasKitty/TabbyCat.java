/* Author: Ayah Alshami
 * CSI 2300
 * April 18 2025
 * defines specific behavior for tabby cat
 * has 4 stages
  */

package TasKitty;

public class TabbyCat extends Pet {
    public TabbyCat(String name) {
        super(name, 4); // TabbyCat has 4 stages
    }

    // returns tabby rather than pet when called
    @Override
    public String getType() {
        return "tabby";
    }

    // overrides with unique stage count
    @Override
    public String getStage() {
        if (getLevel() == 1) return "egg";
        else if (getLevel() == 2) return "baby";
        else if (getLevel() == 3) return "teen";
        else return "adult"; 
    }
}