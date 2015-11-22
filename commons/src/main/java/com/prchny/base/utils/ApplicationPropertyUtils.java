/*
 *  Copyright 2014 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 07-Jul-2014
 *  @author ajinkya
 */

package com.prchny.base.utils;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class ApplicationPropertyUtils {
  
  private ApplicationPropertyUtils() {
  
  }
  
  /**
   * Returns an <code>InetAddress</code> object encapsulating what is most
   * likely the machine's LAN IP address.
   * <p/>
   * This method is intended for use as a replacement of JDK method
   * <code>InetAddress.getLocalHost</code>, because that method is ambiguous on
   * Linux systems. Linux systems enumerate the loopback network interface the
   * same way as regular LAN network interfaces, but the JDK
   * <code>InetAddress.getLocalHost</code> method does not specify the algorithm
   * used to select the address returned under such circumstances, and will
   * often return the loopback address, which is not valid for network
   * communication. Details <a
   * href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4665037">here</a>.
   * <p/>
   * This method will scan all IP addresses on all network interfaces on the
   * host machine to determine the IP address most likely to be the machine's
   * LAN address. If the machine has multiple IP addresses, this method will
   * prefer a site-local IP address (e.g. 192.168.x.x or 10.10.x.x, usually
   * IPv4) if the machine has one (and will return the first site-local address
   * if the machine has more than one), but if the machine does not hold a
   * site-local address, this method will return simply the first non-loopback
   * address found (IPv4 or IPv6).
   * <p/>
   * If this method cannot find a non-loopback address using this selection
   * algorithm, it will fall back to calling and returning the result of JDK
   * method <code>InetAddress.getLocalHost</code>.
   * <p/>
   * Credits (including the awesome javadoc string) :
   * http://stackoverflow.com/a/20418809<br>
   * 
   * @throws UnknownHostException
   *           If the LAN address of the machine cannot be found.
   */
  public static InetAddress getLocalHostLANAddress()
      throws UnknownHostException {
  
    try {
      InetAddress candidateAddress = null;
      // Iterate all NICs (network interface cards)...
      for (final Enumeration<NetworkInterface> ifaces =
          NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
        final NetworkInterface iface = ifaces.nextElement();
        // Iterate all IP addresses assigned to each card...
        for (final Enumeration<InetAddress> inetAddrs =
            iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
          final InetAddress inetAddr = inetAddrs.nextElement();
          // This is so that we pick only the IPV4 addresses.
          // Will have to be changed when SD migrates to IPV6 by passing a flag
          // as argument to this function.
          if (inetAddr instanceof Inet6Address) {
            continue;
          }
          if (!inetAddr.isLoopbackAddress()) {
            if (inetAddr.isSiteLocalAddress()) {
              // Found non-loopback site-local address. Return it immediately...
              return inetAddr;
            } else if (candidateAddress == null) {
              // Found non-loopback address, but not necessarily site-local.
              // Store it as a candidate to be returned if site-local address is
              // not subsequently found...
              candidateAddress = inetAddr;
              // Note that we don't repeatedly assign non-loopback
              // non-site-local addresses as candidates,
              // only the first. For subsequent iterations, candidate will be
              // non-null.
            }
          }
        }
      }
      if (candidateAddress != null) {
        // We did not find a site-local address, but we found some other
        // non-loopback address.
        // Server might have a non-site-local address assigned to its NIC (or it
        // might be running
        // IPv6 which deprecates the "site-local" concept).
        // Return this non-loopback candidate address...
        return candidateAddress;
      }
      // At this point, we did not find a non-loopback address.
      // Fall back to returning whatever InetAddress.getLocalHost() returns...
      final InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
      if (jdkSuppliedAddress == null) {
        throw new UnknownHostException(
            "The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
      }
      return jdkSuppliedAddress;
    } catch (final Exception e) {
      final UnknownHostException unknownHostException =
          new UnknownHostException("Failed to determine LAN address: " + e);
      unknownHostException.initCause(e);
      throw unknownHostException;
    }
  }
  
}
