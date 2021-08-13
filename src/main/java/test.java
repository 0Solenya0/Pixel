import bots.vote.models.Vote;
import bots.xo.MessageHandler;
import org.mariuszgromada.math.mxparser.Expression;
import server.db.HibernateUtil;
import shared.models.Message;
import shared.request.Packet;

import javax.script.ScriptException;


public class test {
    public static String getVoteString(Vote vote) {
        StringBuilder builder = new StringBuilder();
        builder.append("Vote ID: ").append(vote.getVoteId()).append("\n");
        int i = 1;
        for (String option: vote.getOptions())
            builder.append(i++).append("-").append(option).append(" (" + vote.getVoteCount(i - 1) + ")").append('\n');
        return builder.toString();
    }

    public static void main(String[] args) throws ScriptException {
        //Expression expression = new Expression("2 + 2");
        //System.out.println(expression.calculate());
        Vote vote = new Vote();
        vote.setVoteId(1);
        vote.addOption("option 1");
        System.out.println(getVoteString(vote));
        HibernateUtil.HibernateSession session = new HibernateUtil.HibernateSession();
        Message message = ((Message) session.get(Message.class, 149));
        message.setContent(getVoteString(vote));
        session.save(message);
        System.out.println(message.getContent());
        /* session.save(user);
        User user2 = new User();
        user2.setUsername("ghader");
        user2.setPassword("123");
        user.followers.add(user2);
        session.save(user2);*/
    }
}
